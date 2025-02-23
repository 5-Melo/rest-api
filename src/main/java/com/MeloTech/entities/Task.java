package com.MeloTech.entities;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

public class Task {

    @Id
    private String id;
    @NotBlank(message = "Title shouldn't be empty")
    private String title;
    private String description;

    private List<String> labelIds;
    private String statusId;
    private List<String> dependencyIds;
    private String projectId;
    //hours and dates
    @CreatedDate
    private LocalDateTime createDate;//auto set
    private LocalDateTime lastUpdateTime;//auto set
    private LocalDateTime dueDate;//set by user
    private LocalDateTime startDate;//set by user
    private LocalDateTime endDate;//set by user
    private Double estimatedHours;//set by user
    private Double actualHours;//auto set after finish task

    public Task(String id, String title, String description, List<String> labelIds, String statusId, List<String> dependencyIds, LocalDateTime createDate, LocalDateTime lastUpdateTime, LocalDateTime dueDate, LocalDateTime startDate, LocalDateTime endDate, Double estimatedHours, Double actualHours) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.labelIds = labelIds;
        this.statusId = statusId;
        this.dependencyIds = dependencyIds;
        this.createDate = createDate;
        this.lastUpdateTime = lastUpdateTime;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimatedHours = estimatedHours;
        this.actualHours = actualHours;
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public List<String> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<String> labelIds) {
        this.labelIds = labelIds;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public List<String> getDependencyIds() {
        return dependencyIds;
    }

    public void setDependencyIds(List<String> dependencyIds) {
        this.dependencyIds = dependencyIds;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public Double getActualHours() {
        return actualHours;
    }

    public void setActualHours(Double actualHours) {
        this.actualHours = actualHours;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
