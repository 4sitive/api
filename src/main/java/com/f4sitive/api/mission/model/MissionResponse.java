package com.f4sitive.api.mission.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionResponse {
    private String id;
    private String categoryName;
    private String image;
    private String question;
    private String content;
}
