package com.MeloTech.project;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {

    @Id
    private String id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;
    private ArrayList<String> teamMembers;  // Reference username's for each member
    private String projectOwner; // Reference username for user who created the project

    public Project(String title, String description, LocalDate startDate, LocalDate endDate, String projectOwner) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectOwner = projectOwner;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(ArrayList<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    public void addTeamMember(String memberUsername) {
        if (!this.teamMembers.contains(memberUsername)) {
            this.teamMembers.add(memberUsername);
        }
    }

    public void deleteTeamMember(String memberUsername) {
        this.teamMembers.remove(memberUsername);
    }
}
