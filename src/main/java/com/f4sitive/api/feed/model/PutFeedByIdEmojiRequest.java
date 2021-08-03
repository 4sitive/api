package com.f4sitive.api.feed.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class PutFeedByIdEmojiRequest {
    private Set<String> emoji = new LinkedHashSet<>();
}
