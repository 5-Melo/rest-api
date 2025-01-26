package com.MeloTech.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository{
    User findByUsernameAndPassword(String username, String password);

}
