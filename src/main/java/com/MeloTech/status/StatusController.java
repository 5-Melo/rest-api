package com.MeloTech.status;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing statuses.
 */
@RestController
@RequestMapping("api/users/{userId}/projects/{projectId}/statuses")
@Tag(name = "Status Management", description = "APIs for managing statuses within a project")
public class StatusController {
    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    /**
     * Creates a new status in a project.
     *
     * @param projectId The ID of the project.
     * @param status    The status to create.
     * @return A response entity containing the created status or an error message.
     */
    @Operation(
            summary = "Create a new status",
            description = "Creates a new status in the specified project. The status name must be unique within the project.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Status details to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Status.class),
                            examples = @ExampleObject(
                                    value = "{\"name\": \"In Progress\", \"color\": \"#FFFF00\"}"
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Status.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"12345\", \"name\": \"In Progress\", \"color\": \"#FFFF00\", \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Status name already exists in the project",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Status name already exists in this project")
                    )
            )
    })
    @PostMapping("")
    public ResponseEntity<?> createStatus(@PathVariable String projectId, @RequestBody Status status) {
        try {
            return ResponseEntity.ok(statusService.createStatus(projectId, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Gets all statuses in a project.
     *
     * @param projectId The ID of the project.
     * @return A response entity containing the list of statuses in the project.
     */
    @Operation(
            summary = "Get all statuses in a project",
            description = "Retrieves all statuses associated with the specified project ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of statuses retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Status[].class),
                    examples = @ExampleObject(
                            value = "[{\"id\": \"12345\", \"name\": \"In Progress\", \"color\": \"#FFFF00\", \"projectId\": \"project-123\"}]"
                    )
            )
    )
    @GetMapping("")
    public ResponseEntity<List<Status>> getAllStatusesInProject(@PathVariable String projectId) {
        return ResponseEntity.ok(statusService.getStatusesByProjectId(projectId));
    }

    /**
     * Gets a status by its ID and ensures it belongs to the project.
     *
     * @param projectId The ID of the project.
     * @param id        The ID of the status.
     * @return A response entity containing the status if found and it belongs to the project.
     */
    @Operation(
            summary = "Get a status by ID",
            description = "Retrieves a status by its ID and ensures it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Status.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"12345\", \"name\": \"In Progress\", \"color\": \"#FFFF00\", \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Status not found in the project",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Status not found")
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getStatusById(@PathVariable String projectId, @PathVariable String id) {
        return statusService.getStatusById(id, projectId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a status and ensures it belongs to the project.
     *
     * @param projectId    The ID of the project.
     * @param id           The ID of the status to update.
     * @param statusDetails The updated status details.
     * @return A response entity containing the updated status or an error message.
     */
    @Operation(
            summary = "Update a status",
            description = "Updates the status's name and color while ensuring it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Status.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"12345\", \"name\": \"In Progress\", \"color\": \"#00FF00\", \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Project ID cannot be changed or status name already exists",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Status name already exists in this project")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Status not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Status not found")
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable String projectId, @PathVariable String id, @RequestBody Status statusDetails) {
        try {
            if(!statusDetails.getProjectId().equals(projectId)){
                return ResponseEntity.badRequest().body("Project ID cannot be changed.");}
            return ResponseEntity.ok(statusService.updateStatus(id, statusDetails, projectId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a status and ensures it belongs to the project.
     *
     * @param projectId The ID of the project.
     * @param id        The ID of the status to delete.
     * @return A response entity indicating success or failure.
     */
    @Operation(
            summary = "Delete a status",
            description = "Deletes a status if it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Status deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Status does not belong to the project",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Status does not belong to this project")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Status not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Status not found")
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable String projectId, @PathVariable String id) {
        try {
            statusService.deleteStatus(projectId, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}