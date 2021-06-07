package com.f4sitive.api.mission;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MissionResponse {
    private String id;
    private String subject;
    private String body;
}
