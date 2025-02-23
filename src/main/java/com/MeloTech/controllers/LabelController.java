package com.MeloTech.controllers;

import com.MeloTech.entities.Label;
import com.MeloTech.services.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing labels.
 */
@RestController
@RequestMapping("api/users/{userId}/projects/{projectId}/labels")
@Tag(name = "Label", description = "APIs for managing labels within a project")
public class LabelController {
    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    /**
     * Creates a new label in a project.
     *
     * @param projectId The ID of the project.
     * @param label     The label to create.
     * @return A response entity containing the created label or an error message.
     */
    @Operation(
            summary = "Create a new label",
            description = "Creates a new label in the specified project. The label name must be unique within the project.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Label details to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Label.class),
                            examples = @ExampleObject(
                                    value = "{\"name\": \"Bug\", \"color\": \"#FF0000\"}"
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Label created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Label.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"12345\", \"name\": \"Bug\", \"color\": \"#FF0000\", \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Label name already exists in the project",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Label name already exists in this project")
                    )
            )
    })
    @PostMapping("")
    public ResponseEntity<?> createLabel(@PathVariable String projectId, @RequestBody Label label) {
        try {
            return ResponseEntity.ok(labelService.createLabel(projectId, label));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Gets all labels in a project.
     *
     * @param projectId The ID of the project.
     * @return A response entity containing the list of labels in the project.
     */
    @Operation(
            summary = "Get all labels in a project",
            description = "Retrieves all labels associated with the given project ID.")
    @ApiResponse(
            responseCode = "200",
            description = "List of labels retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Label[].class),
                    examples = @ExampleObject(
                            value = "[{\"id\": \"12345\", \"name\": \"Bug\", \"color\": \"#FF0000\", \"projectId\": \"project-123\"}]"
                    )
            )
    )
    @GetMapping("")
    public ResponseEntity<List<Label>> getAllLabelsInProject(@PathVariable String projectId) {
        return ResponseEntity.ok(labelService.getLabelsByProjectId(projectId));
    }

    /**
     * Gets a label by its ID and ensures it belongs to the project.
     *
     * @param projectId The ID of the project.
     * @param id        The ID of the label.
     * @return A response entity containing the label if found and it belongs to the project.
     */
    @Operation(
            summary = "Get a label by ID",
            description = "Retrieves a label by its ID and ensures it belongs to the specified project.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Label found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Label.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Label not found in the project",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Label not found")
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getLabelById(@PathVariable String projectId, @PathVariable String id) {
        return labelService.getLabelById(id, projectId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a label and ensures it belongs to the project.
     *
     * @param projectId    The ID of the project.
     * @param id           The ID of the label to update.
     * @param labelDetails The updated label details.
     * @return A response entity containing the updated label or an error message.
     */
    @Operation(
            summary = "Update a label",
            description = "Updates the label's name and color while ensuring it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Label updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Label.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Project ID cannot be changed or label name already exists",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Label name already exists in this project")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Label not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Label not found")
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLabel(@PathVariable String projectId, @PathVariable String id, @RequestBody Label labelDetails) {
        try {
            if (!labelDetails.getProjectId().equals(projectId)) {
                return ResponseEntity.badRequest().body("Project ID cannot be changed.");
            }
            return ResponseEntity.ok(labelService.updateLabel(id, labelDetails, projectId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a label and ensures it belongs to the project.
     *
     * @param projectId The ID of the project.
     * @param id        The ID of the label to delete.
     * @return A response entity indicating success or failure.
     */
    @Operation(
            summary = "Delete a label",
            description = "Deletes a label if it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Label deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Label does not belong to the project",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Label does not belong to this project")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Label not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Label not found")
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLabel(@PathVariable String projectId, @PathVariable String id) {
        try {
            labelService.deleteLabel(projectId, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}