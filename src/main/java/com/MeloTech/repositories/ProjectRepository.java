package com.MeloTech.repositories;

import com.MeloTech.entities.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    ArrayList<Project> findByIdIn(List<String> projectIds);
    
    @Query("{ $or: [ { 'ownerUserId': ?0 }, { 'teamMemberIds': ?0 } ] }")
    ArrayList<Project> findByOwnerUserIdOrTeamMemberIdsContains(String userId);

    // Find projects where user is the owner
    ArrayList<Project> findByOwnerUserId(String userId);

    // Find projects where user is a team member
    @Query("{ 'teamMemberIds': ?0 }")
    ArrayList<Project> findByTeamMemberIdsContains(String userId);
}
