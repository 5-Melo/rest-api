package com.MeloTech.user;

import com.MeloTech.project.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository {
    User findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByUsername(String username);

    ArrayList<User> findByUsernameIn(List<String> usernames);
    @Query("{ 'username': { $regex: '^?0', $options: 'i' } }")
    ArrayList<User> findByUsernameStartingWith(String prefix);
}
