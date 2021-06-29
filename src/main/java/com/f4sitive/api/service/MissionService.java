package com.f4sitive.api.service;

import com.f4sitive.api.entity.Mission;
import com.f4sitive.api.repository.MissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class MissionService {
    private final MissionRepository missionRepository;

    public MissionService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @Transactional
    public Mono<Mission> save(Mission mission){
        return missionRepository.save(mission);
    }

    @Transactional
    public Mono<Void> deleteById(String id){
        return missionRepository.deleteById(id);
    }
}
