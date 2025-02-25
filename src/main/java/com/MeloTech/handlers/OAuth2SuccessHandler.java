package com.MeloTech.handlers;

import com.MeloTech.entities.User;
import com.MeloTech.enums.AuthProviderEnum;
import com.MeloTech.services.JwtService;
import com.MeloTech.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    public OAuth2SuccessHandler(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");
        String email = oauth2User.getAttribute("email");
        String username = email.split("@")[0];

        User user = null;

        // If user logged in via external auth provider with new email -> save in the database
        if (!userService.existsByEmail(email)) {
            userService.addUser(new User(firstName, lastName, username,
                    null, email, AuthProviderEnum.GOOGLE));
        } else
            user = this.userService.findByEmail(email);

        String token = jwtService.generateToken(user);

        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write JSON response
        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.setStatus(HttpServletResponse.SC_OK);
    }


}