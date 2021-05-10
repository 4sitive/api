package com.foursitive.api.repository;

import com.foursitive.api.entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends MongoRepository<Tag, String> {
    List<Tag> findAllByNameIn(Collection<String> names);
}
