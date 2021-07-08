package com.f4sitive.api.feed.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PutFeedRequest {
    private Map<String, Boolean> emoji;
}
