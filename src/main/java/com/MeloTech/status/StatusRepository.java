package com.MeloTech.status;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing statuses in the database.
 */
@Repository
public interface StatusRepository extends MongoRepository<Status, String> {
    /**
     * Finds a status by its name and project ID.
     *
     * @param name      The name of the status.
     * @param projectId The ID of the project.
     * @return An optional containing the status if found.
     */
    Optional<Status> findByNameAndProjectId(String name, String projectId);

    /**
     * Finds all statuses in a project.
     *
     * @param projectId The ID of the project.
     * @return A list of statuses in the project.
     */
    List<Status> findByProjectId(String projectId);
}