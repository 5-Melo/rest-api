package com.MeloTech.services;


import com.MeloTech.entities.Project;
import com.MeloTech.repositories.ProjectRepository;
import com.MeloTech.entities.User;
import com.MeloTech.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project createProject(String userId, Project project) {
        Optional<User> currentUserOptional = this.userRepository.findById(userId);
        if (currentUserOptional.isEmpty())
            throw new IllegalArgumentException("User with ID " + userId + " not found!");

        User currentUser = currentUserOptional.get();

        project.setProjectOwner(currentUser.getUsername());
        Project newProject = this.projectRepository.save(project);


        List<String> membersOfProject = newProject.getTeamMembers();
        membersOfProject.add(currentUser.getUsername());

        ArrayList<User> users = this.userRepository.findByUsernameIn(membersOfProject);
        users.forEach(user -> {
            user.addProject(newProject.getId());
        });
        this.userRepository.saveAll(users);

        return newProject;

    }

    public void deleteProject(String userId, String projectId) {
        Optional<User> currentUserOptional = this.userRepository.findById(userId);
        if (currentUserOptional.isEmpty())
            throw new IllegalArgumentException("User with ID " + userId + " not found!");

        User currentUser = currentUserOptional.get();

        Optional<Project> projectOptional = this.projectRepository.findById(projectId);

        if (projectOptional.isEmpty())
            throw new IllegalArgumentException("Project with ID " + projectId + " not found!");


        Project project = projectOptional.get();

        currentUser.deleteProject(projectId);
        this.userRepository.save(currentUser);

        project.deleteTeamMember(currentUser.getUsername());

        if (Objects.equals(project.getProjectOwner(), currentUser.getUsername())) { // if he is the owner then remove the project from the database
            ArrayList<User> users = this.userRepository.findByUsernameIn(project.getTeamMembers());
            users.forEach(user -> {
                user.deleteProject(projectId);
            });

            this.userRepository.saveAll(users);
            this.projectRepository.delete(project);

        } else this.projectRepository.save(project);


    }

    public ArrayList<Project> getAllProjects(String userId) {
        Optional<User> currentUserOptional = this.userRepository.findById(userId);
        if (currentUserOptional.isEmpty())
            throw new IllegalArgumentException("User with ID " + userId + " not found!");

        return this.projectRepository.findByIdIn(currentUserOptional.get().getProjectIds());
    }

    public Project updateProject(String userId, String projectId, Project updatedProject) {
        Optional<User> currentUserOptional = this.userRepository.findById(userId);
        if (currentUserOptional.isEmpty())
            throw new IllegalArgumentException("User with ID " + userId + " not found!");

        Optional<Project> projectOptional = this.projectRepository.findById(projectId);

        if (projectOptional.isEmpty())
            throw new IllegalArgumentException("Project with ID " + projectId + " not found!");


        Project existingProject = projectOptional.get();

        ArrayList<User> oldProjectUsers = this.userRepository.findByUsernameIn(existingProject.getTeamMembers());
        oldProjectUsers.forEach(user -> {
            user.deleteProject(existingProject.getId());
        });
        this.userRepository.saveAll(oldProjectUsers);

        existingProject.getTeamMembers().clear();
        // Copy non-null properties from updatedProject to existingProject
        modelMapper.getConfiguration().setSkipNullEnabled(true); // Skip null values
        modelMapper.map(updatedProject, existingProject);

        ArrayList<User> newProjectUsers = this.userRepository.findByUsernameIn(existingProject.getTeamMembers());
        newProjectUsers.forEach(user -> {
            user.addProject(existingProject.getId());
        });
        this.userRepository.saveAll(newProjectUsers);


        return projectRepository.save(existingProject);

    }
}
