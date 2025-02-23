package com.MeloTech.services;

import com.MeloTech.dtos.LoginUserDto;
import com.MeloTech.dtos.RegisterUserDto;
import com.MeloTech.entities.User;
import com.MeloTech.exceptions.EmailAlreadyExistException;
import com.MeloTech.exceptions.UsernameAlreadyTakenException;
import com.MeloTech.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterUserDto registerUserDto) {
        if (this.userRepository.existsByUsername(registerUserDto.getUsername()))
            throw new UsernameAlreadyTakenException("User with this username " + registerUserDto.getUsername() + " already registered in the system");

        if (this.userRepository.existsByEmail(registerUserDto.getEmail()))
            throw new EmailAlreadyExistException("User with this email " + registerUserDto.getEmail() + " already registered in the system");

        User user = new User(registerUserDto.getFirstName(), registerUserDto.getLastName(),
                registerUserDto.getUsername(), passwordEncoder.encode(registerUserDto.getPassword()), registerUserDto.getEmail());
        this.userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername());
    }

}