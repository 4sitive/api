package com.f4sitive.api.repository;

import com.f4sitive.api.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserRepository extends R2dbcRepository<User, String> {
}