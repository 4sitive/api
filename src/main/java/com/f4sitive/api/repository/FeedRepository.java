package com.f4sitive.api.repository;

import com.f4sitive.api.entity.Feed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedRepository extends MongoRepository<Feed, String> {
}
