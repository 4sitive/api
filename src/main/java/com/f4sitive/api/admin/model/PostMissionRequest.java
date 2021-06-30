package com.f4sitive.api.admin.model;

import com.f4sitive.api.entity.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
public class PostMissionRequest {
    private String question;
    private String image;
    private String content;
    private String categoryId;
}
