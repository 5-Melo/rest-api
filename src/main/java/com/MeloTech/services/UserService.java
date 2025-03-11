package com.MeloTech.services;

import com.MeloTech.dtos.UserDto;
import com.MeloTech.entities.User;
import com.MeloTech.exceptions.UserNotFoundException;
import com.MeloTech.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new UserDto(
                    user.getId(), 
                    user.getFirstName(), 
                    user.getLastName(), 
                    user.getUsername(), 
                    user.getEmail()
                ));
    }

    public ArrayList<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(
                    user.getId(), 
                    user.getFirstName(), 
                    user.getLastName(), 
                    user.getUsername(), 
                    user.getEmail()
                ))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public UserDto getUser(String userId) {
        return userRepository.findById(userId)
                .map(user -> new UserDto(
                    user.getId(), 
                    user.getFirstName(), 
                    user.getLastName(), 
                    user.getUsername(), 
                    user.getEmail()
                ))
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    public ArrayList<UserDto> searchUsersWithPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Search prefix cannot be null or empty");
        }
        
        return userRepository.findByUsernameStartingWith(prefix.trim()).stream()
                .map(user -> new UserDto(
                    user.getId(), 
                    user.getFirstName(), 
                    user.getLastName(), 
                    user.getUsername(), 
                    user.getEmail()
                ))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userRepository.existsByUsername(username.trim());
    }

    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return userRepository.existsByEmail(email.trim());
    }

    public User findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        User user = userRepository.findByUsername(username.trim());
        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        User user = userRepository.findByEmail(email.trim());
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user;
    }
}
