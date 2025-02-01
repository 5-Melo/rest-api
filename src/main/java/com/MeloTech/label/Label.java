package com.MeloTech.label;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "labels")
public class Label {
    @Id
    String id;
    @Indexed(unique = true) // Ensures uniqueness in MongoDB
    @NotBlank
    String name;
    @NotBlank
    String color;

    public Label(String id, String name, String color) {
        this.id    = id;
        this.name  = name;
        this.color = color;
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
}
