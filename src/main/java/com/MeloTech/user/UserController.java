package com.MeloTech.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    @Operation(summary = "Get All Users")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved users",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))
            )
    )
    private ResponseEntity<ArrayList<UserResponseDTO>> getAllUsers() {
        ArrayList<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        ArrayList<User> users = (ArrayList<User>) this.userRepository.findAll();

        users.forEach(user -> {
            userResponseDTOS.add(new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail()));
        });
        return ResponseEntity.ok(userResponseDTOS);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user via userId")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User Retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "User not found")
                    )
            )
    })
    private ResponseEntity<?> getUser(@PathVariable String id) {
        Optional<User> optionalUser = this.userRepository.findById(id);


        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
            return ResponseEntity.ok(userResponseDTO);
        }

        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

    }


    @PostMapping("")
    @Operation(summary = "Add a user (SignUp Request)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "SignUp successful"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict in the provided resources",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "username/email already existed")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request (e.g., Missing some attributes)",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Validation failed: Some attributes are missing")
                    )
            )
    })
    private ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        if (this.userRepository.existsByUsername(user.getUsername()))
            return new ResponseEntity<>("username is already taken", HttpStatus.CONFLICT);

        if (this.userRepository.existsByEmail(user.getEmail()))
            return new ResponseEntity<>("email is already registered", HttpStatus.CONFLICT);

        this.userRepository.save(user);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    @Operation(summary = "User login request")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Login Failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "invalid username or password")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request (e.g., missing username/password)",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Validation failed: username/password required")
                    )
            )
    })
    private ResponseEntity<?> loginUser(@Valid @RequestBody LoginCredentials credentials) {
        User user = this.userRepository.findByUsernameAndPassword(credentials.getUsername(), credentials.getPassword());

        if (user == null)
            return new ResponseEntity<>("invalid username or password", HttpStatus.UNAUTHORIZED);

        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());

        return ResponseEntity.ok(userResponseDTO);
    }


    @GetMapping("/search")
    @Operation(summary = "User Search by prefix-matching")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved usernames",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(type = "string"))
                    )
            )
    })

    // returns the list of usernames that prefix-matched the @RequestParam
    private ResponseEntity<ArrayList<String>> searchWithPrefix(@RequestParam String prefix) {
        ArrayList<User> users = this.userRepository.findByUsernameStartingWith(prefix);
        ArrayList<String> listOfUsernames = new ArrayList<>();
        users.forEach(user -> {
            listOfUsernames.add(user.getUsername());
        });
        return ResponseEntity.ok(listOfUsernames);
    }

}
