package com.f4sitive.api.mission.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class GetMissionResponse  {
    private LocalDate now;
    private List<MissionResponse> content;
    public static GetMissionResponse of(List<MissionResponse> content){
        return GetMissionResponse.builder().content(content).now(LocalDate.now()).build();
    }
}
