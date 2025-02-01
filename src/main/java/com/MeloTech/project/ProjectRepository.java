package com.MeloTech.project;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String>, CustomProjectRepository {
    ArrayList<Project> findByIdIn(List<String> projectIds);
}
