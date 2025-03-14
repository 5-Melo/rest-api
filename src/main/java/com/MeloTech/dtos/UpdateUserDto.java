package com.MeloTech.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserDto {
    @Size(min = 3, max = 10, message = "firstName size in range [3-10]")
    private String firstName;

    @Size(min = 3, max = 10, message = "lastName size in range [3-10]")
    private String lastName;

    @Size(min = 3, max = 10, message = "username size in range [3-10]")
    private String username;

    @Email(message = "email format is required")
    private String email;

    // Optional password fields - no validation if not provided
    private String currentPassword;

    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$",
        message = "New password must be at least 5 characters long, include an uppercase letter, a lowercase letter, a number, and a special character [@$!%*?&]"
    )
    private String newPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
} 