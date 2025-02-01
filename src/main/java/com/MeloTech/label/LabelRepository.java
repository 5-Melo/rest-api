package com.MeloTech.label;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends MongoRepository<Label,String> {
    Optional<Label> findByName(String name);

}
