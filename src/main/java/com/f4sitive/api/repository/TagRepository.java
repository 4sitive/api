package com.f4sitive.api.repository;

import com.f4sitive.api.entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends MongoRepository<Tag, String> {
    List<Tag> findAllByNameIn(Collection<String> names);
}
