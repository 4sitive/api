package com.f4sitive.api.service;

import com.f4sitive.api.entity.Category;
import com.f4sitive.api.entity.Mission;
import com.f4sitive.api.repository.CategoryRepository;
import com.f4sitive.api.repository.MissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MissionService {
    private final MissionRepository missionRepository;
    private final CategoryRepository categoryRepository;
    private final MongoOperations mongoOperations;

    public MissionService(MissionRepository missionRepository,
                          CategoryRepository categoryRepository,
                          MongoOperations mongoOperations) {
        this.missionRepository = missionRepository;
        this.categoryRepository = categoryRepository;
        this.mongoOperations = mongoOperations;
    }

    public Mono<Page<Mission>> findAll(Pageable pageable) {
        return Mono.fromCallable(() -> missionRepository.queryAll(LocalDate.now(), pageable))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<List<Mission>> findAll(LocalDate date) {
        return Mono.fromCallable(() -> missionRepository.queryAllByDate(date, date.plus(1L, ChronoUnit.DAYS)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Mission> save(Mission mission) {
        return Mono.fromCallable(() -> {
            mission.setCategory(categoryRepository.findByName(mission.getCategoryName())
                    .orElseGet(() -> categoryRepository.save(Category.of(mission.getCategoryName()))));
            return missionRepository.save(mission);
        })
                .subscribeOn(Schedulers.boundedElastic());
//        return missionRepository.save(mission);
//        return categoryRepository.findById(mission.getCategory().getId())
//                .switchIfEmpty(categoryRepository.save(mission.getCategory()))
//                .flatMap(category -> {
//                    mission.setCategory(category);
////                    category.getMissions().add(mission);
//                    return missionRepository.save(mission);
//                });
    }

    @Transactional
    public Mono<Void> deleteById(String id) {
        return Mono.fromRunnable(() -> missionRepository.deleteById(id));
//        return missionRepository.deleteById(id);
    }
}
