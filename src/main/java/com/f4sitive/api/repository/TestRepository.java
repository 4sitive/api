package com.f4sitive.api.repository;

import com.f4sitive.api.entity.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TestRepository extends ReactiveMongoRepository<Test, String>, TestRepositoryCustom {
}
