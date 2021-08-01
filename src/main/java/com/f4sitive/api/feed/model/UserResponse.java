package com.f4sitive.api.feed.model;

import com.f4sitive.api.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private String id;
    private String image;
    private String name;

    public static UserResponse of(User user){
        return UserResponse.builder()
                .id(user.getId())
                .image(user.getImage())
                .name(user.getUsername())
                .build();
    }
}
