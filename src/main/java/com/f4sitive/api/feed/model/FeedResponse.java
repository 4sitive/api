package com.f4sitive.api.feed.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class FeedResponse {
    private String id;
    private String image;
    private String categoryName;
    private String missionQuestion;
    private UserResponse user;
    private Map<String, Long> emoji;
    private boolean author;
}
