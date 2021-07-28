package com.f4sitive.api.feed.model;

import com.f4sitive.api.entity.Feed;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class GetFeedResponse {
    private String id;
    private String image;
    private String categoryName;
    private String missionQuestion;
    private UserResponse user;
    private Map<String, Long> emoji;
    private boolean author;

    public static GetFeedResponse of(Feed feed){
        return GetFeedResponse.builder()
                .id(feed.getId())
                .categoryName(feed.getMission().getCategory().getName())
                .missionQuestion(feed.getMission().getQuestion())
                .build();
    }
}
