package com.f4sitive.api.admin;

import com.f4sitive.api.admin.model.PostMissionRequest;
import com.f4sitive.api.admin.model.PostMissionResponse;
import com.f4sitive.api.admin.model.PutMissionRequest;
import com.f4sitive.api.entity.Mission;
import com.f4sitive.api.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class AdminController {
    private final MissionService missionService;

    public AdminController(MissionService missionService) {
        this.missionService = missionService;
    }

    @Operation(summary = "미션 등록")
    @PostMapping("/admin/mission")
    public Mono<PostMissionResponse> postMission(@AuthenticationPrincipal Authentication authentication, @RequestBody PostMissionRequest request) {
        if (isAdmin(authentication.getName())) {
            Mission mission = new Mission();
            mission.setContent(request.getContent());
            mission.setImage(request.getImage());
            mission.setQuestion(request.getQuestion());
            mission.setCategoryName(request.getCategoryName());
            mission.setDate(request.getDate());
            return missionService.save(mission).map(PostMissionResponse::of);
        } else {
            return Mono.empty();
        }
    }

    @PutMapping("/admin/mission/{id}")
    public void putMission(@PathVariable String id, @RequestBody PutMissionRequest request) {

    }

    @DeleteMapping("/admin/mission/{id}")
    public Mono<Void> deleteMission(@AuthenticationPrincipal Authentication authentication, @PathVariable String id) {
        if (isAdmin(authentication.getName())) {
            return missionService.deleteById(id);
        } else {
            return Mono.empty();
        }
    }

    boolean isAdmin(String name) {
        return "bf8add58-9bab-4c41-a34f-f353d85d6ea2".equals(name);
    }
}
