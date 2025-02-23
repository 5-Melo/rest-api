package com.MeloTech.repositories;

import com.MeloTech.entities.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    // Find all tasks in a project
    List<Task> findByProjectId(String projectId);

    Optional<Task>findByTitleAndProjectId(String name ,String projectId);
    /**
     * Finds a task by its ID and project ID.
     *
     * @param id        The ID of the task.
     * @param projectId The ID of the project.
     * @return An optional containing the task if found and it belongs to the project.
     */
    Optional<Task> findByIdAndProjectId(String id, String projectId);

    //Find all tasks that contain this task id in dependency
    List<Task> findByDependencyIdsContaining(String taskId);

    // Find tasks by project ID and status ID
    List<Task> findByProjectIdAndStatusId(String projectId, String statusId);

    // Find tasks by project ID and label ID
    @Query("{ 'projectId': ?0, 'labelIds': ?1 }")
    List<Task> findByProjectIdAndLabelId(String projectId, String labelId);

    // Find tasks by project ID, status ID, and label ID
    @Query("{ 'projectId': ?0, 'statusId': ?1, 'labelIds': ?2 }")
    List<Task> findByProjectIdAndStatusIdAndLabelId(String projectId, String statusId, String labelId);
}
