package com.MeloTech.entities;

import com.MeloTech.enums.ProjectStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {

    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatusEnum projectStatusEnum;
    private ArrayList<String> teamMemberIds;  // Reference user IDs for each member
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String ownerUserId; // Reference user ID for user who created the project

    public Project(String title, String description, LocalDate startDate, LocalDate endDate, String ownerUserId) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ownerUserId = ownerUserId;
        this.teamMemberIds = new ArrayList<>();
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ProjectStatusEnum getStatus() {
        return projectStatusEnum;
    }

    public void setStatus(ProjectStatusEnum projectStatusEnum) {
        this.projectStatusEnum = projectStatusEnum;
    }

    public ArrayList<String> getTeamMemberIds() {
        return teamMemberIds;
    }

    public void setTeamMemberIds(ArrayList<String> teamMemberIds) {
        this.teamMemberIds = teamMemberIds;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public void addTeamMemberId(String userId) {
        if (!this.teamMemberIds.contains(userId)) {
            this.teamMemberIds.add(userId);
        }
    }

    public void deleteTeamMemberId(String userId) {
        this.teamMemberIds.remove(userId);
    }
}
