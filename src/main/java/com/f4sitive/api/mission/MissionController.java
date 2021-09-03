package com.f4sitive.api.mission;

import com.f4sitive.api.mission.model.GetMissionResponse;
import com.f4sitive.api.mission.model.GetTodayMissionResponse;
import com.f4sitive.api.mission.model.MissionResponse;
import com.f4sitive.api.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MissionController {
    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @Operation(description = "미션 조회")
    @GetMapping("/missions")
    public Mono<GetMissionResponse> getMission(@PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        return missionService.findAll(pageable)
                .map(page -> GetMissionResponse.of(page.map(MissionResponse::of).getContent()));
    }

    @Operation(description = "투데이 미션 조회")
    @GetMapping("/today/missions")
    public Mono<GetMissionResponse> getTodayMission() {
        return missionService.findAll(LocalDate.now())
                .map(list -> GetMissionResponse.of(list.stream().map(MissionResponse::of).collect(Collectors.toList())));
    }
}
