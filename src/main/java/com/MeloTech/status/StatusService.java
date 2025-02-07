package com.MeloTech.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing statuses.
 */
@Service
public class StatusService {
    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    /**
     * Creates a new status in a project.
     *
     * @param projectId The ID of the project.
     * @param status    The status to create.
     * @return The created status.
     * @throws IllegalArgumentException If a status with the same name already exists in the project.
     */
    public Status createStatus(String projectId, Status status) {
        validateStatusNameUniqueness(status.getName(), projectId);
        Status completedStatus=new Status(status.getName(),status.getColor(),projectId);
        return statusRepository.save(completedStatus);
    }

    /**
     * Gets all statuses in a project.
     *
     * @param projectId The ID of the project.
     * @return A list of statuses in the project.
     */
    public List<Status> getStatusesByProjectId(String projectId) {
        return statusRepository.findByProjectId(projectId);
    }

    /**
     * Gets a status by its ID and ensures it belongs to the project.
     *
     * @param id        The ID of the status.
     * @param projectId The ID of the project.
     * @return An optional containing the status if found and it belongs to the project.
     */
    public Optional<Status> getStatusById(String id, String projectId) {
        return statusRepository.findById(id)
                .filter(status -> status.getProjectId().equals(projectId));
    }

    /**
     * Updates a status and ensures it belongs to the project.
     *
     * @param id           The ID of the status to update.
     * @param statusDetails The updated status details.
     * @param projectId    The ID of the project.
     * @return The updated status.
     * @throws IllegalArgumentException If a status with the same name already exists in the project or if the status does not belong to the project.
     * @throws RuntimeException         If the status is not found.
     */
    public Status updateStatus(String id, Status statusDetails, String projectId) {
        return statusRepository.findById(id)
                .map(status -> {
                    // Ensure the status belongs to the project
                    if (!status.getProjectId().equals(projectId)) {
                        throw new IllegalArgumentException("Status does not belong to this project");
                    }

                    // Validate status name uniqueness
                    validateStatusNameUniqueness(statusDetails.getName(), projectId, id);

                    // Update status fields
                    status.setName(statusDetails.getName());
                    status.setColor(statusDetails.getColor());
                    return statusRepository.save(status);
                })
                .orElseThrow(() -> new RuntimeException("Status not found"));
    }

    /**
     * Deletes a status and ensures it belongs to the project.
     *
     * @param projectId The ID of the project.
     * @param id        The ID of the status to delete.
     * @throws IllegalArgumentException If the status does not belong to the project.
     * @throws RuntimeException         If the status is not found.
     */
    public void deleteStatus(String projectId, String id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        // Ensure the status belongs to the project
        if (!status.getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("Status does not belong to this project");
        }

        statusRepository.delete(status);
    }

    /**
     * Validates that a status name is unique within a project.
     *
     * @param name      The status name to validate.
     * @param projectId The ID of the project.
     * @throws IllegalArgumentException If a status with the same name already exists in the project.
     */
    private void validateStatusNameUniqueness(String name, String projectId) {
        if (statusRepository.findByNameAndProjectId(name, projectId).isPresent()) {
            throw new IllegalArgumentException("Status name already exists in this project");
        }
    }

    /**
     * Validates that a status name is unique within a project, excluding the current status.
     *
     * @param name      The status name to validate.
     * @param projectId The ID of the project.
     * @param excludeId The ID of the status to exclude from the check.
     * @throws IllegalArgumentException If a status with the same name already exists in the project.
     */
    private void validateStatusNameUniqueness(String name, String projectId, String excludeId) {
        statusRepository.findByNameAndProjectId(name, projectId)
                .ifPresent(status -> {
                    if (!status.getId().equals(excludeId)) {
                        throw new IllegalArgumentException("Status name already exists in this project");
                    }
                });
    }
}