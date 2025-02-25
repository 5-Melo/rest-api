package com.MeloTech.services;

import com.MeloTech.dtos.LoginUserDto;
import com.MeloTech.dtos.RegisterUserDto;
import com.MeloTech.entities.User;
import com.MeloTech.enums.AuthProviderEnum;
import com.MeloTech.exceptions.EmailAlreadyExistException;
import com.MeloTech.exceptions.UsernameAlreadyTakenException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserService userService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userService=userService;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterUserDto registerUserDto) {
        if (this.userService.existsByUsername(registerUserDto.getUsername()))
            throw new UsernameAlreadyTakenException("User with this username " + registerUserDto.getUsername() + " already registered in the system");

        if (this.userService.existsByEmail(registerUserDto.getEmail()))
            throw new EmailAlreadyExistException("User with this email " + registerUserDto.getEmail() + " already registered in the system");

        User user = new User(registerUserDto.getFirstName(), registerUserDto.getLastName(),
                registerUserDto.getUsername(), passwordEncoder.encode(registerUserDto.getPassword()),
                registerUserDto.getEmail(), AuthProviderEnum.LOCAL);

        this.userService.addUser(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userService.findByUsername(input.getUsername());
    }

}