package com.f4sitive.api.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostMissionRequest {
    private String question;
    private String image;
    private String content;
    private String categoryName;
    private LocalDate date;
}
