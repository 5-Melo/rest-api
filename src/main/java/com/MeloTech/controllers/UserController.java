package com.MeloTech.controllers;

import com.MeloTech.dtos.LoginUserDto;
import com.MeloTech.dtos.UserDto;
import com.MeloTech.entities.User;
import com.MeloTech.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@Tag(name = "Users", description = "APIs for managing users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    @Operation(summary = "Get All Users")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved users",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
            )
    )

    private ResponseEntity<ArrayList<UserDto>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user via userId")
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
    private ResponseEntity<UserDto> getUser(@PathVariable String id) {
        return ResponseEntity.ok(this.userService.getUser(id));

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
        return ResponseEntity.ok(this.userService.searchWithPrefix(prefix));
    }

}
