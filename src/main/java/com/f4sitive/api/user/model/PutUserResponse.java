package com.f4sitive.api.user.model;

import com.f4sitive.api.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PutUserResponse {
    private String image;
    private String introduce;
    private String name;

    public static PutUserResponse of(User user) {
        return PutUserResponse.builder()
                .image(user.getImage())
                .introduce(user.getIntroduce())
                .name(user.getName())
                .build();
    }
}
