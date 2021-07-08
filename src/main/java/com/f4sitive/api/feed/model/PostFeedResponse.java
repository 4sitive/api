package com.f4sitive.api.feed.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostFeedResponse {
    private String id;
    private String image;
    private String categoryName;
    private String missionQuestion;
}
