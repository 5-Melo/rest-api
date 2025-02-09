package com.MeloTech.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing tasks within a project.
 */
@RestController
@RequestMapping("api/users/{userId}/projects/{projectId}/tasks")
@Tag(name = "Task Management", description = "APIs for managing tasks within a project")
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
     * @return A response entity containing the created task or an error message.
     */
    @Operation(
            summary = "Create a new task",
            description = "Creates a new task in the specified project.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Task details to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(
                                    value = "{\"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-123\", \"labelIds\": [\"label-456\"], \"dependencyIds\": [\"task-789\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5}"
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"task-123\", \"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-123\", \"labelIds\": [\"label-456\"], \"dependencyIds\": [\"task-789\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5, \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or task creation failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Invalid task details provided")
                    )
            )
    })
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
    @Operation(
            summary = "Get all tasks in a project",
            description = "Retrieves all tasks in the specified project, optionally filtered by status or label."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of tasks retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Task[].class),
                    examples = @ExampleObject(
                            value = "[{\"id\": \"task-123\", \"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-123\", \"labelIds\": [\"label-456\"], \"dependencyIds\": [\"task-789\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5, \"projectId\": \"project-123\"}]"
                    )
            )
    )
    @GetMapping("")
    public ResponseEntity<?> getAllTasksInProject(
            @PathVariable String projectId,
            @RequestParam(required = false) String statusId,
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
    @Operation(
            summary = "Get a task by ID",
            description = "Retrieves a task by its ID and ensures it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"task-123\", \"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-123\", \"labelIds\": [\"label-456\"], \"dependencyIds\": [\"task-789\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5, \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found in the project",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Task not found")
                    )
            )
    })
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
    @Operation(
            summary = "Update a task",
            description = "Updates the task's details while ensuring it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"task-123\", \"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-123\", \"labelIds\": [\"label-456\"], \"dependencyIds\": [\"task-789\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5, \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or task update failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Invalid task details provided")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Task not found")
                    )
            )
    })
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
    @Operation(
            summary = "Update a task's status",
            description = "Updates the status of a task while ensuring it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task status updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"task-123\", \"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-456\", \"labelIds\": [\"label-456\"], \"dependencyIds\": [\"task-789\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5, \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status ID or task update failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Invalid status ID provided")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Task not found")
                    )
            )
    })
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
     * Adds a label to a task.
     *
     * @param projectId The ID of the project.
     * @param taskId    The ID of the task.
     * @param labelId   The ID of the label to add.
     * @return A response entity containing the updated task.
     */
    @Operation(
            summary = "Add a label to a task",
            description = "Adds a label to a task while ensuring it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Label added to task successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"task-123\", \"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-123\", \"labelIds\": [\"label-456\", \"label-789\"], \"dependencyIds\": [\"task-789\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5, \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid label ID or task update failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Invalid label ID provided")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Task not found")
                    )
            )
    })
    @PatchMapping("/{taskId}/add-label")
    public ResponseEntity<?> addLabelToTask(
            @PathVariable String projectId,
            @PathVariable String taskId,
            @RequestParam String labelId) {
        try {
            return ResponseEntity.ok(taskService.addLabel(projectId, taskId, labelId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Removes a label from a task.
     *
     * @param projectId The ID of the project.
     * @param taskId    The ID of the task.
     * @param labelId   The ID of the label to remove.
     * @return A response entity containing the updated task.
     */
    @Operation(
            summary = "Remove a label from a task",
            description = "Removes a label from a task while ensuring it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Label removed from task successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"task-123\", \"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-123\", \"labelIds\": [\"label-456\"], \"dependencyIds\": [\"task-789\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5, \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid label ID or task update failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Invalid label ID provided")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Task not found")
                    )
            )
    })
    @PatchMapping("/{taskId}/remove-label")
    public ResponseEntity<?> removeLabelFromTask(
            @PathVariable String projectId,
            @PathVariable String taskId,
            @RequestParam String labelId) {
        try {
            return ResponseEntity.ok(taskService.removeLabel(projectId, taskId, labelId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Adds a dependency to a task.
     *
     * @param projectId    The ID of the project.
     * @param taskId       The ID of the task.
     * @param dependencyId The ID of the dependency task.
     * @return A response entity containing the updated task.
     */
    @Operation(
            summary = "Add a dependency to a task",
            description = "Adds a dependency to a task while ensuring it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dependency added to task successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"task-123\", \"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-123\", \"labelIds\": [\"label-456\"], \"dependencyIds\": [\"task-789\", \"task-456\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5, \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid dependency ID or task update failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Invalid dependency ID provided")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Task not found")
                    )
            )
    })
    @PatchMapping("/{taskId}/add-dependency")
    public ResponseEntity<?> addDependencyToTask(
            @PathVariable String projectId,
            @PathVariable String taskId,
            @RequestParam String dependencyId) {
        try {
            return ResponseEntity.ok(taskService.addDependencyToTask(projectId, taskId, dependencyId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Removes a dependency from a task.
     *
     * @param projectId    The ID of the project.
     * @param taskId       The ID of the task.
     * @param dependencyId The ID of the dependency task.
     * @return A response entity containing the updated task.
     */
    @Operation(
            summary = "Remove a dependency from a task",
            description = "Removes a dependency from a task while ensuring it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dependency removed from task successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"task-123\", \"title\": \"Fix Bug\", \"description\": \"Fix the critical bug in the login module\", \"statusId\": \"status-123\", \"labelIds\": [\"label-456\"], \"dependencyIds\": [\"task-789\"], \"dueDate\": \"2023-12-31\", \"startDate\": \"2023-10-01\", \"endDate\": \"2023-10-15\", \"estimatedHours\": 10, \"actualHours\": 5, \"projectId\": \"project-123\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid dependency ID or task update failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Invalid dependency ID provided")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Task not found")
                    )
            )
    })
    @PatchMapping("/{taskId}/remove-dependency")
    public ResponseEntity<?> removeDependencyFromTask(
            @PathVariable String projectId,
            @PathVariable String taskId,
            @RequestParam String dependencyId) {
        try {
            return ResponseEntity.ok(taskService.removeDependencyfromTask(projectId, taskId, dependencyId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Deletes a task.
     *
     * @param projectId The ID of the project.
     * @param taskId    The ID of the task to delete.
     * @return A response entity indicating success.
     */
    @Operation(
            summary = "Delete a task",
            description = "Deletes a task while ensuring it belongs to the specified project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Task deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Task does not belong to the project",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Task does not belong to this project")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Task not found")
                    )
            )
    })
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
