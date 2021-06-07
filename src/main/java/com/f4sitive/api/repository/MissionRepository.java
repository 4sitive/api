package com.f4sitive.api.repository;

import com.f4sitive.api.entity.Mission;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MissionRepository extends MongoRepository<Mission, String> {
    @Aggregation(value = "{$sample: {size: ?0} }", collation = "mission")
    List<Mission> sample(int size);
}
