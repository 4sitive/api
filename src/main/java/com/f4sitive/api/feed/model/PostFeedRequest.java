package com.f4sitive.api.feed.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PostFeedRequest {
    private String missionId;
    private String image;
    private String requestId;
}
