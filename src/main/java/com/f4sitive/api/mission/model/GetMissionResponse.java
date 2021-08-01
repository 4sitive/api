package com.f4sitive.api.mission.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class GetMissionResponse  {
    private LocalDate date;
    private Map<String, List<MissionResponse>> content;
    public static GetMissionResponse of(Map<String, List<MissionResponse>> content){
        return GetMissionResponse.builder().content(content).date(LocalDate.now()).build();
    }
}
