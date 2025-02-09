package com.MeloTech.status;

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