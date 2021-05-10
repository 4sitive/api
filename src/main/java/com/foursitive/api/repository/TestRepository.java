package com.foursitive.api.repository;

import com.foursitive.api.entity.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TestRepository extends MongoRepository<Test, String> {
}
