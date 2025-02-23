package com.MeloTech.controllers;

import com.MeloTech.dtos.LoginUserDto;
import com.MeloTech.dtos.RegisterUserDto;
import com.MeloTech.dtos.UserDto;
import com.MeloTech.entities.User;
import com.MeloTech.services.AuthenticationService;
import com.MeloTech.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/auth")
@RestController
@Tag(name = "Auth", description = "APIs for managing Authentication and Authorization")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registration Request")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "SignUp successful"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict in the provided resources",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                                        {
                                            "type": "about:blank",
                                            "title": "Conflict",
                                            "status": 409,
                                            "detail": "Username or email already exists",
                                            "instance": "/api/auth/register",
                                            "description": "Username/email already existed"
                                        }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Validation error or internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                                        {
                                            "type": "about:blank",
                                            "title": "Internal Server Error",
                                            "status": 500,
                                            "detail": "Validation failed for argument [0] in request with errors",
                                            "instance": "/api/auth/register",
                                            "description": "Validation failed: Some attributes are missing"
                                        }
                                    """)
                    )
            )
    })
    private ResponseEntity<Void> addUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        this.authenticationService.register(registerUserDto);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    @Operation(summary = "User login request")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful - Returns JWT Token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                        {
                                            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2N2JiMjk5M2EwNjU2ZDMxYmNlYTk4YmUiLCJzdWIiOiJtYWdlZDEyMyIsImlhdCI6MTc0MDMxOTcwMCwiZXhwIjoxNzQwNDA2MTAwfQ.T6BtB7Xjiy2vHxlS46rBW47dyPhcVQnaPimrVudhbzE"
                                        }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                                        {
                                            "type": "about:blank",
                                            "title": "Unauthorized",
                                            "status": 401,
                                            "detail": "Invalid username or password",
                                            "instance": "/api/auth/login",
                                            "description": "The username or password is incorrect"
                                        }
                                    """)
                    )
            )
    })
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return ResponseEntity.ok(response);
    }


}
