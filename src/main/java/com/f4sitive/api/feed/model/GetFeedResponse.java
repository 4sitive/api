package com.f4sitive.api.feed.model;

import com.f4sitive.api.entity.Feed;
import com.f4sitive.api.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

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

    public static GetFeedResponse of(Feed feed, String userId){
        User user = feed.getUser();
        return GetFeedResponse.builder()
                .id(feed.getId())
                .categoryName(feed.getCategory().getName())
                .missionQuestion(feed.getMission().getQuestion())
                .user(UserResponse.of(user))
                .author(user.getId().equals(userId))
                .build();
    }
}
