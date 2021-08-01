package com.f4sitive.api.mission;

import com.f4sitive.api.mission.model.GetMissionResponse;
import com.f4sitive.api.mission.model.GetTodayMissionResponse;
import com.f4sitive.api.mission.model.MissionResponse;
import com.f4sitive.api.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
public class MissionController {
    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @Operation(description = "미션 조회")
    @GetMapping("/missions")
    public Mono<GetMissionResponse> getMission(@PageableDefault Pageable pageable) {
//        return missionService.findAll(pageable)
//                .subscribeOn(Schedulers.boundedElastic());
        return missionService.findAll(pageable)
                .map(Page::getContent)
                .<Map<String, List<MissionResponse>>>map(content -> content.stream()
                        .collect(LinkedMultiValueMap::new,
                                (map, entry) -> map.add(entry.getDate(), MissionResponse.of(entry)),
                                Map::putAll))
                .map(GetMissionResponse::of);
    }

    @Operation(description = "투데이 미션 조회")
    @GetMapping("/daymotion/today/missions")
    public Mono<GetTodayMissionResponse> getTodayMission() {
        return Mono.just(GetTodayMissionResponse.builder().build());
    }
}
