package com.f4sitive.api.mission;

import com.f4sitive.api.entity.Mission;
import com.f4sitive.api.repository.MissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/daymotion/missions")
public class MissionController {
    private final MissionRepository missionRepository;

    public MissionController(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @PostMapping
    public MissionResponse post(@RequestBody Mission request) {
        Mission mission = missionRepository.save(request);
        return MissionResponse.builder()
                .body(mission.getBody())
                .id(mission.getId())
                .subject(mission.getSubject())
                .build();
    }

    @GetMapping("/today")
    public Page<MissionResponse> getToday(Pageable pageable) {
        List<Mission> missions = missionRepository.sample(pageable.getPageSize());
        return PageableExecutionUtils.getPage(missions, pageable, missions::size)
                .map(mission -> MissionResponse.builder()
                        .body(mission.getBody())
                        .id(mission.getId())
                        .subject(mission.getSubject())
                        .build());
    }
}
