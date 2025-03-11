package com.MeloTech.repositories;

import com.MeloTech.entities.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    ArrayList<Project> findByIdIn(List<String> projectIds);
    
    @Query("{ $or: [ { 'projectOwner': ?0 }, { 'teamMembers': ?0 } ] }")
    ArrayList<Project> findByProjectOwnerOrTeamMembersContains(String username);
}
