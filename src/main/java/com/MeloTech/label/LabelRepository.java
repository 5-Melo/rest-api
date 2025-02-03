package com.MeloTech.label;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing labels in the database.
 */
@Repository
public interface LabelRepository extends MongoRepository<Label, String> {
    /**
     * Finds a label by its name and project ID.
     *
     * @param name      The name of the label.
     * @param projectId The ID of the project.
     * @return An optional containing the label if found.
     */
    Optional<Label> findByNameAndProjectId(String name, String projectId);

    /**
     * Finds all labels in a project.
     *
     * @param projectId The ID of the project.
     * @return A list of labels in the project.
     */
    List<Label> findByProjectId(String projectId);
}