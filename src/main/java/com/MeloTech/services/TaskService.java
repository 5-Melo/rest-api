package com.MeloTech.services;

import com.MeloTech.entities.Label;
import com.MeloTech.repositories.LabelRepository;
import com.MeloTech.entities.Status;
import com.MeloTech.repositories.StatusRepository;
import com.MeloTech.entities.Task;
import com.MeloTech.repositories.TaskRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final LabelRepository labelRepository;

    @Autowired

    public TaskService(TaskRepository taskRepository, LabelRepository labelRepository, StatusRepository statusRepository) {
        this.taskRepository = taskRepository;
        this.labelRepository = labelRepository;
        this.statusRepository = statusRepository;
    }

    /**
     * Creates a new task in a project.
     *
     * @param projectId The ID of the project.
     * @param task      The task to create.
     * @return The created task.
     */
    public Task createTask(String projectId, Task task) {
        if (taskRepository.findByTitleAndProjectId(task.getTitle(), projectId).isPresent()) {
            throw new IllegalArgumentException("Task name already exit");
        }
        task.setCreateDate(LocalDateTime.now()); // Set creation timestamp
        task.setLastUpdateTime(LocalDateTime.now()); // Set last update timestamp
        task.setProjectId(projectId);
        return taskRepository.save(task);
    }

    /**
     * Gets all tasks in a project.
     *
     * @param projectId The ID of the project.
     * @return A list of tasks in the project.
     */
    public List<Task> getTasksByProjectId(String projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    /**
     * Get Specific task form project
     *
     * @param id        The ID of the Task
     * @param projectId The ID of the Project
     * @return Optional Task if found and it belong to the project
     */
    public Optional<Task> getTaskByIdAndProjectId(String id, String projectId) {
        return taskRepository.findByIdAndProjectId(id, projectId).filter((task -> task.getProjectId().equals(projectId)));
    }

    //====================================filters====================================//
    public List<Task> getFilteredTasks(String projectId, String statusId, String labelId) {
        if (statusId != null && labelId != null) {
            return getTasksByProjectIdAndStatusIdAndLabelId(projectId, statusId, labelId);
        } else if (statusId != null) {
            return getTasksByProjectIdAndStatusId(projectId, statusId);
        } else if (labelId != null) {
            return getTasksByProjectIdAndLabelId(projectId, labelId);
        } else {
            return getTasksByProjectId(projectId);
        }
    }

    /**
     * Gets tasks in a project filtered by status.
     *
     * @param projectId The ID of the project.
     * @param statusId  The ID of the status.
     * @return A list of tasks with the specified status.
     */
    public List<Task> getTasksByProjectIdAndStatusId(String projectId, String statusId) {
        return taskRepository.findByProjectIdAndStatusId(projectId, statusId);
    }

    /**
     * Gets tasks in a project filtered by label.
     *
     * @param projectId The ID of the project.
     * @param labelId   The ID of the label.
     * @return A list of tasks with the specified label.
     */
    public List<Task> getTasksByProjectIdAndLabelId(String projectId, String labelId) {
        return taskRepository.findByProjectIdAndLabelId(projectId, labelId);
    }

    /**
     * Gets tasks in a project filtered by status and label.
     *
     * @param projectId The ID of the project.
     * @param statusId  The ID of the status.
     * @param labelId   The ID of the label.
     * @return A list of tasks with the specified status and label.
     */
    public List<Task> getTasksByProjectIdAndStatusIdAndLabelId(String projectId, String statusId, String labelId) {
        return taskRepository.findByProjectIdAndStatusIdAndLabelId(projectId, statusId, labelId);
    }

//====================================filters====================================//


    //====================================Updates====================================//

    /**
     * Updates a task.
     *
     * @param projectId   The ID of the project.
     * @param taskId      The ID of the task to update.
     * @param taskDetails The updated task details.
     * @return The updated task.
     * @throws RuntimeException         If the task is not found.
     * @throws IllegalArgumentException If the task does not belong to the project.
     */
    public Task updateTask(String projectId, String taskId, Task taskDetails) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    // Ensure the task belongs to the project
                    if (!task.getProjectId().equals(projectId)) {
                        throw new IllegalArgumentException("Task does not belong to this project");
                    }

                    // Update task fields only if they are not null
                    if (taskDetails.getTitle() != null) {
                        task.setTitle(taskDetails.getTitle());
                    }
                    if (taskDetails.getDescription() != null) {
                        task.setDescription(taskDetails.getDescription());
                    }
                    if (taskDetails.getLabelIds() != null) {
                        task.setLabelIds(taskDetails.getLabelIds());
                    }
                    if (taskDetails.getStatusId() != null) {
                        task.setStatusId(taskDetails.getStatusId());
                    }
                    if (taskDetails.getDependencyIds() != null) {
                        task.setDependencyIds(taskDetails.getDependencyIds());
                    }
                    if (taskDetails.getDueDate() != null) {
                        task.setDueDate(taskDetails.getDueDate());
                    }
                    if (taskDetails.getStartDate() != null) {
                        task.setStartDate(taskDetails.getStartDate());
                    }
                    if (taskDetails.getEndDate() != null) {
                        task.setEndDate(taskDetails.getEndDate());
                    }
                    if (taskDetails.getEstimatedHours() != null) {
                        task.setEstimatedHours(taskDetails.getEstimatedHours());
                    }
                    if (taskDetails.getActualHours() != null) {
                        task.setActualHours(taskDetails.getActualHours());
                    }

                    // Update the last update timestamp
                    task.setLastUpdateTime(LocalDateTime.now());

                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    /**
     * Updates a task's status, ensuring they belong to the same project.
     *
     * @param projectId The ID of the project.
     * @param taskId    The ID of the task to update.
     * @param statusId  (Optional) The new status ID for the task.
     * @return The updated task.
     * @throws RuntimeException If the task, status are invalid.
     */
    public Task updateTaskStatus(String projectId, String taskId, String statusId) {
        // Find the task and ensure it belongs to the project
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new RuntimeException("Task not found in this project"));
        if (statusId != null) {
            Status status = statusRepository.findByIdAndProjectId(statusId, projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Status not found in this project"));
            task.setStatusId(status.getId());
        }
        // Update the last update timestamp
        task.setLastUpdateTime(LocalDateTime.now());
        return taskRepository.save(task);
    }

    /**
     * add label to task (label must be predefined and in same project with task).
     *
     * @param projectId The ID of the project.
     * @param taskId    The ID of the task to update.
     * @param labelId   The new label ID for the task.
     * @return the updated task
     * @throws
     */
    public Task addLabel(@NotNull String projectId, @NotNull String taskId, @NotNull String labelId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId).orElseThrow(() -> new RuntimeException("Task not found in this project"));
        //validate label
        Label label = labelRepository.findByIdAndProjectId(labelId, projectId).orElseThrow(() -> new RuntimeException("this Label not found in this project"));
        //if task was created without labels
        if (task.getLabelIds() == null) {
            task.setLabelIds(new ArrayList<>());
        }
        if (!task.getLabelIds().contains(labelId)) {
            task.getLabelIds().add(labelId);
        }
        return taskRepository.save(task);
    }

    /**
     * remove label from task (label must be predefined and in same project with task).
     *
     * @param projectId The ID of the project.
     * @param taskId    The ID of the task to update.
     * @param labelId   The new label ID for the task.
     * @return the updated task
     * @throws
     */
    public Task removeLabel(@NotNull String projectId, @NotNull String taskId, @NotNull String labelId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId).orElseThrow(() -> new RuntimeException("Task not found in this project"));
        //validate label
        Label label = labelRepository.findByIdAndProjectId(labelId, projectId).orElseThrow(() -> new RuntimeException("this Label not found in this project"));
        //if task was created without labels
        if (task.getLabelIds() == null) {
            task.setLabelIds(new ArrayList<>());
        }
        task.getLabelIds().remove(labelId);
        return taskRepository.save(task);
    }

    public Task addDependencyToTask(@NotNull String projectId, @NotNull String taskId, @NotNull String dependencyId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new RuntimeException("Task not found in this project"));

        Task dependency = taskRepository.findByIdAndProjectId(dependencyId, projectId)
                .orElseThrow(() -> new RuntimeException("Dependency task not found in this project"));

        if (task.getDependencyIds() == null) {
            task.setDependencyIds(new ArrayList<>());
        }
        if (!task.getDependencyIds().contains(dependency.getId())) {
            task.getDependencyIds().add(dependency.getId());
        }
        return taskRepository.save(task);
    }

    public Task removeDependencyfromTask(@NotNull String projectId, @NotNull String taskId, @NotNull String dependencyId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new RuntimeException("Task not found in this project"));

        Task dependency = taskRepository.findByIdAndProjectId(dependencyId, projectId)
                .orElseThrow(() -> new RuntimeException("Dependency task not found in this project"));

        if (task.getDependencyIds() == null) {
            task.setDependencyIds(new ArrayList<>());
        }
        task.getDependencyIds().remove(dependency.getId());

        return taskRepository.save(task);
    }
    public Task addAssignee(String projectId, String taskId, String assigneeId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new RuntimeException("Task not found in this project"));

        if (task.getAssigneeIds() == null) {
            task.setAssigneeIds(new ArrayList<>());
        }


        if (!task.getAssigneeIds().contains(assigneeId)) {
            task.getAssigneeIds().add(assigneeId);
        }


        task.setLastUpdateTime(LocalDateTime.now());
        return taskRepository.save(task);
    }
    public Task removeAssignee(String projectId, String taskId, String assigneeId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new RuntimeException("Task not found in this project"));

        if (task.getAssigneeIds() != null) {
            task.getAssigneeIds().remove(assigneeId);
        }

        task.setLastUpdateTime(LocalDateTime.now());
        return taskRepository.save(task);
    }
    //====================================Updates====================================//

    //====================================Delete====================================//

    /**
     * Deletes a task.
     *
     * @param id The ID of the task to delete.
     */
    public void deleteTask(String projectId, String id) {
        Task deletedTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        // Ensure the label belongs to the project
        if (!deletedTask.getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("Task does not belong to this project");
        }
        //all tasks that depend on the deleted task
        List<Task> dependentTasks = taskRepository.findByDependencyIdsContaining(id);
        //remove task reference for deleted task
        for (Task task : dependentTasks) {
            task.getDependencyIds().remove(id);
        }
        taskRepository.saveAll(dependentTasks);
        taskRepository.deleteById(id);
    }
    //====================================Delete====================================//

}
