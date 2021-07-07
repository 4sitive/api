package com.f4sitive.api.mission.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class GetMissionResponse  {
    private Map<String, List<MissionResponse>> content;
}
