package com.MeloTech.controllers;

import com.MeloTech.dtos.LoginUserDto;
import com.MeloTech.dtos.UserDto;
import com.MeloTech.entities.User;
import com.MeloTech.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/users")
@Tag(name = "Users", description = "APIs for managing users")
@Validated
public class UserController {
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @Operation(summary = "Get All Users", description = "Retrieve all users with pagination support")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved users",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid page parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)
            )
        )
    })
    public ResponseEntity<?> getAllUsers(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        
        Page<UserDto> userPage = userService.getAllUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(userPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user via userId", description = "Retrieve a specific user by their ID")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User Retrieved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class)
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
    public ResponseEntity<UserDto> getUser(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable String id) {
        return ResponseEntity.ok(this.userService.getUser(id));
    }

    @GetMapping("/search")
    @Operation(
        summary = "User Search by prefix-matching",
        description = "Search for users whose username starts with the given prefix"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved users",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid or empty prefix",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)
            )
        )
    })
    public ResponseEntity<ArrayList<UserDto>> searchWithPrefix(
            @Parameter(description = "Username prefix to search for", required = true)
            @RequestParam String prefix) {
        return ResponseEntity.ok(this.userService.searchUsersWithPrefix(prefix));
    }
}
