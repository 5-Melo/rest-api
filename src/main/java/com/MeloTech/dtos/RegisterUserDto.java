package com.MeloTech.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.index.Indexed;

public class RegisterUserDto {
    @NotBlank(message = "firstName shouldn't be empty")
    @Size(min = 3, max = 10, message = "firstName size in range [3-10]")
    private String firstName;

    @NotBlank(message = "lastName shouldn't be empty")
    @Size(min = 3, max = 10, message = "lastName size in range [3-10]")
    private String lastName;

    @NotBlank(message = "username shouldn't be empty")
    @Size(min = 3, max = 10, message = "username size in range [3-10]")
    @Indexed(unique = true) // Index on username
    private String username;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$",
            message = "Password must be at least 5 characters long, include an uppercase letter, a lowercase letter, a number, and a special character [@$!%*?&]"
    )

    private String password;

    @NotBlank(message = "email shouldn't be empty")
    @Email(message = "email format is required")
    private String email;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
