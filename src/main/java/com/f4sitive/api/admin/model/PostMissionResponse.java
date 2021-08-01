package com.f4sitive.api.admin.model;

import com.f4sitive.api.entity.Mission;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostMissionResponse {
    private String id;
    private String categoryName;

    public static PostMissionResponse of(Mission mission) {
        return PostMissionResponse.builder()
                .id(mission.getId())
                .categoryName(mission.getCategory().getName())
                .build();
    }
}
