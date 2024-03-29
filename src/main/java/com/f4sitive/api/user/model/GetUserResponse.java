package com.f4sitive.api.user.model;

import com.f4sitive.api.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUserResponse {
    private String id;
    private String image;
    private String introduce;
    private String name;
    private String username;

    public static GetUserResponse of(User user) {
        return GetUserResponse.builder()
                .id(user.getId())
                .image(user.getImage())
                .name(user.getName())
                .username(user.getUsername())
                .introduce(user.getIntroduce())
                .build();
    }
}
