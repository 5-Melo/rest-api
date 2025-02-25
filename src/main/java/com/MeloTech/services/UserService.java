package com.MeloTech.services;

import com.MeloTech.dtos.UserDto;
import com.MeloTech.entities.User;
import com.MeloTech.exceptions.UserNotFoundException;
import com.MeloTech.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ArrayList<UserDto> getAllUsers() {
        ArrayList<UserDto> userDtos = new ArrayList<>();
        ArrayList<User> users = (ArrayList<User>) this.userRepository.findAll();

        users.forEach(user -> {
            userDtos.add(new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail()));
        });

        return userDtos;
    }

    public UserDto getUser(String userId) {
        Optional<User> optionalUser = this.userRepository.findById(userId);


        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
            return userDto;
        }

        throw new UserNotFoundException("User not found with ID: " + userId);

    }


    public ArrayList<String> searchWithPrefix(String prefix) {
        ArrayList<User> users = this.userRepository.findByUsernameStartingWith(prefix);
        ArrayList<String> listOfUsernames = new ArrayList<>();
        users.forEach(user -> {
            listOfUsernames.add(user.getUsername());
        });
        return listOfUsernames;
    }

    public void addUser(User user) {
        this.userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

}
