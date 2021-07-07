package com.f4sitive.api.mission.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetTodayMissionResponse {
    private List<MissionResponse> content;
}
