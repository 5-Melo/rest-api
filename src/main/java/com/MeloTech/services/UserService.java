package com.MeloTech.services;

import com.MeloTech.dtos.UserDto;
import com.MeloTech.dtos.UpdateUserDto;
import com.MeloTech.entities.User;
import com.MeloTech.exceptions.UserNotFoundException;
import com.MeloTech.repositories.UserRepository;
import com.MeloTech.repositories.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
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

    @Transactional
    public UserDto updateUser(String userId, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Handle password update if provided
        if (updateUserDto.getNewPassword() != null) {
            if (updateUserDto.getCurrentPassword() == null) {
                throw new IllegalArgumentException("Current password is required to update password");
            }
            
            // Verify current password
            if (!passwordEncoder.matches(updateUserDto.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
            
            // Update password
            user.setPassword(passwordEncoder.encode(updateUserDto.getNewPassword()));
        }

        // Update other fields if provided
        if (updateUserDto.getFirstName() != null) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null) {
            user.setLastName(updateUserDto.getLastName());
        }
        if (updateUserDto.getUsername() != null && !updateUserDto.getUsername().equals(user.getUsername())) {
            // Check if username is already taken by another user
            if (userRepository.existsByUsername(updateUserDto.getUsername())) {
                throw new IllegalArgumentException("Username is already taken");
            }
            
            // Update username in user entity
            user.setUsername(updateUserDto.getUsername());
        }
        
        if (updateUserDto.getEmail() != null) {
            // Check if email is already taken by another user
            if (userRepository.existsByEmail(updateUserDto.getEmail()) && 
                !user.getEmail().equals(updateUserDto.getEmail())) {
                throw new IllegalArgumentException("Email is already taken");
            }
            user.setEmail(updateUserDto.getEmail());
        }

        User updatedUser = userRepository.save(user);
        return new UserDto(
            updatedUser.getId(),
            updatedUser.getFirstName(),
            updatedUser.getLastName(),
            updatedUser.getUsername(),
            updatedUser.getEmail()
        );
    }
}
