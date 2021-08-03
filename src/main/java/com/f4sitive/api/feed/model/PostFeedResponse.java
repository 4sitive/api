package com.f4sitive.api.feed.model;

import com.f4sitive.api.entity.Feed;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostFeedResponse {
    private String id;
    private String image;
    private String categoryName;
    private String missionQuestion;

    public static PostFeedResponse of(Feed feed) {
        return PostFeedResponse.builder()
                .id(feed.getId())
                .missionQuestion(feed.getMission().getQuestion())
                .categoryName(feed.getCategory().getName())
                .build();
    }
}
