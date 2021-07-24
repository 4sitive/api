package com.f4sitive.api.user.model;

import com.f4sitive.api.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUserByIdResponse {
    private String id;
    private String image;
    private String introduce;

    public static GetUserByIdResponse of(User user) {
        return GetUserByIdResponse.builder()
                .id(user.getId())
                .image(user.getImage())
                .introduce(user.getIntroduce())
                .build();
    }
}
