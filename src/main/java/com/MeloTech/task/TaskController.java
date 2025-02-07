package com.MeloTech.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/users/{userId}/projects/{projectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Creates a new task in a project.
     *
     * @param projectId The ID of the project.
     * @param task      The task to create.
     * @return A response entity containing the created task.
     */
    @PostMapping
    public ResponseEntity<?> createTask(@PathVariable String projectId, @RequestBody Task task) {

        try {
            return ResponseEntity.ok(taskService.createTask(projectId, task));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Gets all tasks in a project, optionally filtered by status or label.
     *
     * @param projectId The ID of the project.
     * @param statusId  (Optional) The ID of the status to filter by.
     * @param labelId   (Optional) The ID of the label to filter by.
     * @return A response entity containing the list of tasks.
     */
    @GetMapping("")
    public ResponseEntity<?> getAllTasksInProject(@PathVariable String projectId,
                                                  @RequestParam (required = false)String statusId,
                                                  @RequestParam(required = false) String labelId) {
        List<Task> tasks = taskService.getFilteredTasks(projectId, statusId, labelId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Gets a task by its ID and ensures it belongs to the project.
     *
     * @param projectId The ID of the project.
     * @param id        The ID of the task.
     * @return A response entity containing the task if found and it belongs to the project.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable String projectId, @PathVariable String id) {
        return taskService.getTaskByIdAndProjectId(id, projectId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a task.
     *
     * @param projectId   The ID of the project.
     * @param taskId      The ID of the task to update.
     * @param taskDetails The updated task details.
     * @return A response entity containing the updated task or an error message.
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable String projectId,
            @PathVariable String taskId,
            @RequestBody Task taskDetails) {
        try {
            Task updatedTask = taskService.updateTask(projectId, taskId, taskDetails);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates a task's status.
     *
     * @param projectId The ID of the project.
     * @param taskId    The ID of the task to update.
     * @param statusId  The new status ID for the task.
     * @return A response entity containing the updated task.
     */
    @PatchMapping("/{taskId}/update-status")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable String projectId,
            @PathVariable String taskId,
            @RequestParam String statusId) {
        try {
            return ResponseEntity.ok(taskService.updateTaskStatus(projectId, taskId, statusId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates a task's labels.
     *
     * @param projectId The ID of the project.
     * @param taskId    The ID of the task to update.
     * @param labelIds  The new list of label IDs for the task.
     * @return A response entity containing the updated task.
     */
    @PatchMapping("/{taskId}/update-labels")
    public ResponseEntity<?> updateTaskLabels(
            @PathVariable String projectId,
            @PathVariable String taskId,
            @RequestParam List<String> labelIds) {
        try {
            return ResponseEntity.ok(taskService.updateTaskLabels(projectId, taskId, labelIds));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a task.
     *
     * @param projectId The ID of the project.
     * @param taskId    The ID of the task to delete.
     * @return A response entity indicating success.
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable String projectId,
            @PathVariable String taskId) {
        try {
            taskService.deleteTask(projectId, taskId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
