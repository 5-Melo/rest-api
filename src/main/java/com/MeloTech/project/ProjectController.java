package com.MeloTech.project;

import com.MeloTech.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/users/{userId}/projects")
@Tag(name = "Projects", description = "Operations related to projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping("")
    @Operation(summary = "Get all projects owned by the User")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Projects retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Project.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "User with the provided ID doesn't exist")
                    )
            )
    })
    private ResponseEntity<?> getAllProjects(@PathVariable String userId) {
        try {
            ArrayList<Project> projects = this.projectService.getAllProjects(userId);
            return ResponseEntity.status(HttpStatus.OK).body(projects);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("")
    @Operation(summary = "User creates a project")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Project created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Project.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User/Project not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "User/Project with the provided ID doesn't exist")
                    )
            )
    })
    private ResponseEntity<?> createProject(@PathVariable String userId, @RequestBody Project project) {
        try {
            Project newProject = this.projectService.createProject(userId, project);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "User deletes a project")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Project deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User/Project not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "User/Project with the provided ID doesn't exist")
                    )
            )
    })
    private ResponseEntity<?> deleteProject(@PathVariable String userId, @PathVariable String projectId) {
        try {
            this.projectService.deleteProject(userId, projectId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "User updates a project")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Project updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Project.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User/Project not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "User/Project with the provided ID doesn't exist")
                    )
            )
    })
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
