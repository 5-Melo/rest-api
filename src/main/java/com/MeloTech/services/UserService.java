package com.MeloTech.services;

import com.MeloTech.dtos.LoginUserDto;
import com.MeloTech.dtos.UserDto;
import com.MeloTech.entities.User;
import com.MeloTech.exceptions.BadCredentialsException;
import com.MeloTech.exceptions.EmailAlreadyExistException;
import com.MeloTech.exceptions.UserNotFoundException;
import com.MeloTech.exceptions.UsernameAlreadyTakenException;
import com.MeloTech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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

    public void register(User user) {
        if (this.userRepository.existsByUsername(user.getUsername()))
            throw new UsernameAlreadyTakenException("User with this username " + user.getUsername() + " already registered in the system");

        if (this.userRepository.existsByEmail(user.getEmail()))
            throw new EmailAlreadyExistException("User with this email " + user.getEmail() + " already registered in the system");

        this.userRepository.save(user);
    }

    public UserDto login(LoginUserDto loginUserDto) {
        User user = this.userRepository.findByUsernameAndPassword(loginUserDto.getUsername(), loginUserDto.getPassword());

        if (user == null)
            throw new BadCredentialsException("username or password is incorrect!");

        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
    }

    public ArrayList<String> searchWithPrefix(String prefix) {
        ArrayList<User> users = this.userRepository.findByUsernameStartingWith(prefix);
        ArrayList<String> listOfUsernames = new ArrayList<>();
        users.forEach(user -> {
            listOfUsernames.add(user.getUsername());
        });
        return listOfUsernames;
    }

}
