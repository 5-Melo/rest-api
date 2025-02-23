package com.MeloTech.repositories;

import com.MeloTech.entities.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    ArrayList<Project> findByIdIn(List<String> projectIds);
}
