package com.f4sitive.api.repository;

import com.f4sitive.api.entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends ReactiveMongoRepository<Tag, String> {
    List<Tag> findAllByNameIn(Collection<String> names);
}
