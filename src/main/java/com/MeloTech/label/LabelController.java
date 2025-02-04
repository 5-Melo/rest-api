package com.MeloTech.label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing labels.
 */
@RestController
@RequestMapping("users/{userId}/projects/{projectId}/labels")
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
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLabel(@PathVariable String projectId, @PathVariable String id, @RequestBody Label labelDetails) {
        try {
            if (!labelDetails.getProjectId().equals(projectId)){
                return ResponseEntity.badRequest().body("Project ID cannot be changed.");
            }
            return ResponseEntity.ok(labelService.updateLabel(id, labelDetails, projectId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (RuntimeException e){
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