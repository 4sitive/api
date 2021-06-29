package com.f4sitive.api.mission;

import com.f4sitive.api.entity.Mission;
import com.f4sitive.api.repository.MissionRepository;
import com.f4sitive.api.service.MissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MissionController {
    private final MissionRepository missionRepository;
    private final MissionService missionService;

    public MissionController(MissionRepository missionRepository,
                             MissionService missionService) {
        this.missionRepository = missionRepository;
        this.missionService = missionService;
    }

    @GetMapping("/daymotion/missions")
    public void get(@PageableDefault Pageable pageable){

    }

    @PostMapping("/daymotion/missions")
    public MissionResponse post(@RequestBody Mission request) {
        Mission mission = missionRepository.save(request);
        return MissionResponse.builder()
//                .body(mission.getBody())
                .id(mission.getId())
//                .subject(mission.getSubject())
                .build();
    }

    @GetMapping("/daymotion/today/missions")
    public Page<MissionResponse> getToday(Pageable pageable) {
        List<Mission> missions = missionRepository.sample(pageable.getPageSize());
        return PageableExecutionUtils.getPage(missions, pageable, missions::size)
                .map(mission -> MissionResponse.builder()
//                        .body(mission.getBody())
                        .id(mission.getId())
//                        .subject(mission.getSubject())
                        .build());
    }
}
