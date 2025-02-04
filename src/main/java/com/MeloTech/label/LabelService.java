package com.MeloTech.label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing labels.
 */
@Service
public class LabelService {
    private final LabelRepository labelRepository;

    @Autowired
    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    /**
     * Creates a new label in a project.
     *
     * @param projectId The ID of the project.
     * @param label     The label to create.
     * @return The created label.
     * @throws IllegalArgumentException If a label with the same name already exists in the project.
     */
    public Label createLabel(String projectId, Label label) {
        validateLabelNameUniqueness(label.getName(), projectId);
        Label completedLabel=new Label(label.getName(), label.getColor(),projectId);
        return labelRepository.save(completedLabel);
    }

    /**
     * Gets all labels in a project.
     *
     * @param projectId The ID of the project.
     * @return A list of labels in the project.
     */
    public List<Label> getLabelsByProjectId(String projectId) {
        return labelRepository.findByProjectId(projectId);
    }

    /**
     * Gets a label by its ID and ensures it belongs to the project.
     *
     * @param id        The ID of the label.
     * @param projectId The ID of the project.
     * @return An optional containing the label if found and it belongs to the project.
     */
    public Optional<Label> getLabelById(String id, String projectId) {
        return labelRepository.findById(id)
                .filter(label -> label.getProjectId().equals(projectId));
    }

    /**
     * Updates a label and ensures it belongs to the project.
     *
     * @param id           The ID of the label to update.
     * @param labelDetails The updated label details.
     * @param projectId    The ID of the project.
     * @return The updated label.
     * @throws IllegalArgumentException If a label with the same name already exists in the project.
     * @throws RuntimeException         If the label is not found or does not belong to the project.
     */
    public Label updateLabel(String id, Label labelDetails, String projectId) {
        return labelRepository.findById(id)
                .map(label -> {
                    // Ensure the label belongs to the project
                    if (!label.getProjectId().equals(projectId)) {
                        throw new IllegalArgumentException("Label does not belong to this project");
                    }

                    // Validate label name uniqueness
                    validateLabelNameUniqueness(labelDetails.getName(), projectId, id);

                    // Update label fields
                    label.setName(labelDetails.getName());
                    label.setColor(labelDetails.getColor());
                    return labelRepository.save(label);
                })
                .orElseThrow(() -> new RuntimeException("Label not found"));
    }

    /**
     * Deletes a label and ensures it belongs to the project.
     *
     * @param projectId The ID of the project.
     * @param id        The ID of the label to delete.
     * @throws IllegalArgumentException If the label does not belong to the project.
     * @throws RuntimeException         If the label is not found.
     */
    public void deleteLabel(String projectId, String id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Label not found"));

        // Ensure the label belongs to the project
        if (!label.getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("Label does not belong to this project");
        }

        labelRepository.delete(label);
    }


    /**
     * Validates that a label name is unique within a project.
     *
     * @param name      The label name to validate.
     * @param projectId The ID of the project.
     * @throws IllegalArgumentException If a label with the same name already exists in the project.
     */
    private void validateLabelNameUniqueness(String name, String projectId) {
        if (labelRepository.findByNameAndProjectId(name, projectId).isPresent()) {
            throw new IllegalArgumentException("Label name already exists in this project");
        }
    }

    /**
     * Validates that a label name is unique within a project, excluding the current label.
     *
     * @param name      The label name to validate.
     * @param projectId The ID of the project.
     * @param excludeId The ID of the label to exclude from the check.
     * @throws IllegalArgumentException If a label with the same name already exists in the project.
     */
    private void validateLabelNameUniqueness(String name, String projectId, String excludeId) {
        labelRepository.findByNameAndProjectId(name, projectId)
                .ifPresent(label -> {
                    if (!label.getId().equals(excludeId)) {
                        throw new IllegalArgumentException("Label name already exists in this project");
                    }
                });
    }
}