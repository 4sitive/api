package com.f4sitive.api.feed.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class PutFeedResponse {
    private String id;
    private Map<String, Long> emoji;
}
