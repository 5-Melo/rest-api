package com.MeloTech.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    private ArrayList<User> getAllUsers() {
        return (ArrayList<User>) this.userRepository.findAll();
    }

    @GetMapping("/{id}")
    private ResponseEntity<Optional<User>> getUser(@PathVariable String id) {
        Optional<User> user = this.userRepository.findById(id);

        if (user.isPresent())
            return new ResponseEntity<>(user, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    private void addUser(@Valid @RequestBody User user) {
        this.userRepository.save(user);
    }

    @PostMapping("/login")
    private ResponseEntity<?> loginUser(@RequestBody User credentials) {
        User user = this.userRepository.findByUsernameAndPassword(credentials.getUsername(), credentials.getPassword());

        if (user == null)
            return new ResponseEntity<>("invalid username or password", HttpStatus.UNAUTHORIZED);

        return ResponseEntity.ok(user);
    }


}
