package com.f4sitive.api.feed.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private String id;
    private String image;
    private String name;
}
