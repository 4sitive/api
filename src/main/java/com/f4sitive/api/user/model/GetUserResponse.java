package com.f4sitive.api.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUserResponse {
    private String id;
    private String image;
    private String introduce;
}
