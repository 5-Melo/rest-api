package com.MeloTech.project;

import org.springframework.data.mongodb.core.MongoTemplate;

public class CustomProjectRepositoryImpl implements CustomProjectRepository {
    private final MongoTemplate mongoTemplate;

    public CustomProjectRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
