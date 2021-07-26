package com.f4sitive.api.admin;

import com.f4sitive.api.admin.model.PostMissionRequest;
import com.f4sitive.api.admin.model.PutMissionRequest;
import com.f4sitive.api.entity.Category;
import com.f4sitive.api.entity.Mission;
import com.f4sitive.api.service.MissionService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class AdminController {
    private final MissionService missionService;

    public AdminController(MissionService missionService) {
        this.missionService = missionService;
    }

    @Operation(summary = "미션 등록")
    @PostMapping("/admin/mission")
    public Mono<Mission> postMission(@RequestBody PostMissionRequest request) {
        Mission mission = new Mission();
        mission.setContent(request.getContent());
        mission.setImage(request.getImage());
        mission.setQuestion(request.getQuestion());
        mission.setCategoryName(request.getCategoryName());
        return missionService.save(mission);
    }

    @PutMapping("/admin/mission/{id}")
    public void putMission(@PathVariable String id, @RequestBody PutMissionRequest request) {

    }

    @DeleteMapping("/admin/mission/{id}")
    public Mono<Void> deleteMission(@PathVariable String id) {
        return missionService.deleteById(id);
    }
}
