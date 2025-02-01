package com.MeloTech.project;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/users/{userId}/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping("")
    private ResponseEntity<?> getAllProjects(@PathVariable String userId) {
        try {
            ArrayList<Project> projects = this.projectService.getAllProjects(userId);
            return ResponseEntity.status(HttpStatus.OK).body(projects);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("")
    private ResponseEntity<?> createProject(@PathVariable String userId, @RequestBody Project project) {
        try {
            Project newProject = this.projectService.createProject(userId, project);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{projectId}")
    private ResponseEntity<?> deleteProject(@PathVariable String userId, @PathVariable String projectId) {
        try {
            Project deleteProject = this.projectService.deleteProject(userId, projectId);
            return ResponseEntity.status(HttpStatus.OK).body(deleteProject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{projectId}")
    private ResponseEntity<?> updateProject(@PathVariable String userId, @PathVariable String projectId,
                                            @RequestBody Project updatedProject) {
        try {
            Project updated = this.projectService.updateProject(userId, projectId, updatedProject);
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
