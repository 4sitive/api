package com.f4sitive.api.mission.model;

import com.f4sitive.api.entity.Mission;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MissionResponse {
    private String id;
    private String categoryName;
    private String image;
    private String question;
    private String content;
    private LocalDate date;
    public static MissionResponse of(Mission mission){
        return MissionResponse.builder()
                .id(mission.getId())
                .categoryName(mission.getCategory().getName())
                .image(mission.getImage())
                .question(mission.getQuestion())
                .content(mission.getContent())
                .date(mission.getDate())
                .build();
    }
}
