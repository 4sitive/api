package com.f4sitive.api.service;

import com.f4sitive.api.entity.Category;
import com.f4sitive.api.entity.Mission;
import com.f4sitive.api.repository.CategoryRepository;
import com.f4sitive.api.repository.MissionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MissionService {
    private final MissionRepository missionRepository;
    private final CategoryRepository categoryRepository;

    public MissionService(MissionRepository missionRepository,
                          CategoryRepository categoryRepository) {
        this.missionRepository = missionRepository;
        this.categoryRepository = categoryRepository;
    }

    public Mono<List<Mission>> findAll(Pageable pageable){
        return Mono.fromCallable(() -> {
            return missionRepository.findAll(pageable).getContent();
        });
//        return Flux.fromIterable(missionRepository.findAll(pageable));
    }

    @Transactional
    public Mono<Mission> save(Mission mission){
        return Mono.fromCallable(() -> {
            mission.setCategory(categoryRepository.findByName(mission.getCategoryName())
                    .orElseGet(() -> categoryRepository.save(Category.of(mission.getCategoryName()))));
            return missionRepository.save(mission);
        });
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
    public Mono<Void> deleteById(String id){
        return Mono.fromRunnable(() -> missionRepository.deleteById(id));
//        return missionRepository.deleteById(id);
    }
}
