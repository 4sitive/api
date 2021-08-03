package com.f4sitive.api.feed.model;

import com.f4sitive.api.entity.Feed;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
public class PutFeedByIdEmojiResponse {
    private String id;
    private Map<String, Long> emoji;

    public static PutFeedByIdEmojiResponse of(Feed feed) {
        Map<String, Long> emoji = feed.getEmoji()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return PutFeedByIdEmojiResponse.builder().id(feed.getId())
                .emoji(emoji)
                .build();
    }
}
