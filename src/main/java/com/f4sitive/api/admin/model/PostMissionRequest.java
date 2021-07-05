package com.f4sitive.api.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMissionRequest {
    private String question;
    private String image;
    private String content;
    private String categoryName;
}
