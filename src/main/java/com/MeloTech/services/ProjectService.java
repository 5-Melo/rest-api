package com.MeloTech.services;


import com.MeloTech.entities.Project;
import com.MeloTech.repositories.ProjectRepository;
import com.MeloTech.entities.User;
import com.MeloTech.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
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

        project.setOwnerUserId(currentUser.getId());
        Project newProject = this.projectRepository.save(project);

        // Add owner as a team member
        if (newProject.getTeamMemberIds() == null) {
            newProject.setTeamMemberIds(new ArrayList<>());
        }
        newProject.addTeamMemberId(currentUser.getId());
        newProject = this.projectRepository.save(newProject);

        // Update user's project list
        currentUser.addProject(newProject.getId());
        this.userRepository.save(currentUser);

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

        // Only project owner can delete the project
        if (!Objects.equals(project.getOwnerUserId(), currentUser.getId())) {
            throw new IllegalArgumentException("Only project owner can delete the project");
        }

        // Remove project from all team members' project lists
        ArrayList<User> users = this.userRepository.findByIdIn(project.getTeamMemberIds());
        users.forEach(user -> {
            user.deleteProject(projectId);
        });

        this.userRepository.saveAll(users);
        this.projectRepository.delete(project);
    }

    public ArrayList<Project> getAllProjects(String userId) {
        Optional<User> currentUserOptional = this.userRepository.findById(userId);
        if (currentUserOptional.isEmpty())
            throw new IllegalArgumentException("User with ID " + userId + " not found!");

        return this.projectRepository.findByOwnerUserIdOrTeamMemberIdsContains(userId);
    }

    public Project updateProject(String userId, String projectId, Project updatedProject) {
        Optional<User> currentUserOptional = this.userRepository.findById(userId);
        if (currentUserOptional.isEmpty())
            throw new IllegalArgumentException("User with ID " + userId + " not found!");

        Optional<Project> projectOptional = this.projectRepository.findById(projectId);

        if (projectOptional.isEmpty())
            throw new IllegalArgumentException("Project with ID " + projectId + " not found!");

        Project existingProject = projectOptional.get();

        // Only project owner can update the project
        if (!Objects.equals(existingProject.getOwnerUserId(), userId)) {
            throw new IllegalArgumentException("Only project owner can update the project");
        }

        // Remove project from old team members' project lists
        ArrayList<User> oldProjectUsers = this.userRepository.findByIdIn(existingProject.getTeamMemberIds());
        oldProjectUsers.forEach(user -> {
            user.deleteProject(existingProject.getId());
        });
        this.userRepository.saveAll(oldProjectUsers);

        existingProject.getTeamMemberIds().clear();
        // Copy non-null properties from updatedProject to existingProject
        modelMapper.getConfiguration().setSkipNullEnabled(true); // Skip null values
        modelMapper.map(updatedProject, existingProject);

        // Add project to new team members' project lists
        ArrayList<User> newProjectUsers = this.userRepository.findByIdIn(existingProject.getTeamMemberIds());
        newProjectUsers.forEach(user -> {
            user.addProject(existingProject.getId());
        });
        this.userRepository.saveAll(newProjectUsers);

        return projectRepository.save(existingProject);
    }
}
