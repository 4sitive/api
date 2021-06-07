package com.f4sitive.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mission")
@Getter
@Setter
public class Mission {
    @Id
    private String id;
    private String subject;
    private String body;
}
