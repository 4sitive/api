package com.f4sitive.api.repository;

import com.f4sitive.api.entity.Feed;
import com.f4sitive.api.entity.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedRepository extends MongoRepository<Feed, String> {
}
