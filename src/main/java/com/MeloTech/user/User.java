package com.MeloTech.user;

import com.MeloTech.project.Project;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public class User {

    @Id
    private String id;

    @NotBlank(message = "firstName shouldn't be empty")
    @Size(min = 3,max = 10 ,message = "firstName size in range [3-10]")
    private String firstName;
    @NotBlank(message = "lastName shouldn't be empty")
    @Size(min = 3,max = 10 ,message = "lastName size in range [3-10]")
    private String lastName;
    @NotBlank(message = "username shouldn't be empty")
    @Size(min = 3,max = 10 ,message = "username size in range [3-10]")
    private String username;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$",
            message = "Password must be at least 5 characters long, include an uppercase letter, a lowercase letter, a number, and a special character"
    )
    private String password;
    @NotBlank(message = "email shouldn't be empty")
    @Email(message = "email format is required")
    private String email;
    private ArrayList<Project> projectList;

    public User(String firstName, String lastName, String username, String password,
                String email, ArrayList<Project> projectList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.projectList = projectList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public ArrayList<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(ArrayList<Project> projectList) {
        this.projectList = projectList;
    }
}
