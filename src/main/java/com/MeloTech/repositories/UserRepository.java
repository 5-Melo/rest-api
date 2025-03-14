package com.MeloTech.repositories;

import com.MeloTech.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByUsername(String username);

    User findByEmail(String email);

    ArrayList<User> findByUsernameIn(List<String> usernames);
    @Query("{ 'username': { $regex: '^?0', $options: 'i' } }")
    ArrayList<User> findByUsernameStartingWith(String prefix);

    ArrayList<User> findByIdIn(List<String> userIds);
}
