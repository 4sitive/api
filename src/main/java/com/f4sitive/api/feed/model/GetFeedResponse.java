package com.f4sitive.api.feed.model;

import com.f4sitive.api.entity.Feed;
import com.f4sitive.api.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetFeedResponse {
    private String id;
    private String image;
    private String categoryName;
    private String categoryId;
    private String missionQuestion;
    private String missionId;
    private UserResponse user;
    private Map<String, Long> emoji;
    private Collection<String> myEmojis;
    private boolean author;

    public static GetFeedResponse of(Feed feed, String userId){
        Map<String, Long> emoji = feed.getEmoji()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        User user = feed.getUser();
        return GetFeedResponse.builder()
                .id(feed.getId())
                .categoryName(feed.getCategory().getName())
                .categoryId(feed.getCategory().getId())
                .missionQuestion(feed.getMission().getQuestion())
                .missionId(feed.getMission().getId())
                .user(UserResponse.of(user))
                .emoji(emoji)
                .myEmojis(Optional.ofNullable(feed.getEmoji().get(userId)).orElse(Collections.emptySet()))
                .image(feed.getImage())
                .author(user.getId().equals(userId))
                .build();
    }
}
