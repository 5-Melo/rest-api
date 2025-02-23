package com.MeloTech.entities;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "labels")
public class Label {
    @Id
    private String id;
    @NotBlank(message = "Label name is required")
    private String name;
    @NotBlank(message = "Label color is required")
    private String color;

    @NotBlank(message = "Project ID is required")
    private final String projectId;//marked as final to prevent change it

    public Label( String name, String color, String projectId) {
        this.name = name;
        this.color = color;
        this.projectId = projectId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProjectId() {
        return projectId;
    }
}
