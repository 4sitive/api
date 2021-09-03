package com.f4sitive.api.repository;

import com.f4sitive.api.entity.Mission;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

public interface MissionRepository extends MongoRepository<Mission, String> {
    @Aggregation(value = "{$sample: {size: ?0} }", collation = "mission")
    List<Mission> sample(int size);

    Page<Mission> findAllByDateIsNotNullAndCategoryIsNotNull(Pageable pageable);

    List<Mission> findAllByDateBetween(LocalDate start, LocalDate end);

    @Query("{'date' : { $gte: ?0, $lt: ?1 } }")
    List<Mission> queryAllByDate(LocalDate start, LocalDate end);

//    default Flux<Mission> custom(){
//        return findAll(Example.of(new Mission(null, "", null, null, null)));
//    }
}
