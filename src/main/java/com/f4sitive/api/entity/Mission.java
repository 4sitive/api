package com.f4sitive.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mission")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mission {
    @Id
    private String id;
    private String question;
    private String image;
    private String content;
    @DBRef(lazy=true)
    private Category category;
}
