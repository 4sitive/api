package com.f4sitive.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    @DBRef(lazy = true)
    private Set<Feed> feeds = new HashSet<>();

    public static Category of(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
