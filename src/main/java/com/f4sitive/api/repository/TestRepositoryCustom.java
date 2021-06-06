package com.f4sitive.api.repository;

import com.f4sitive.api.entity.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Map;

public interface TestRepositoryCustom {
    Slice<Test> test(Pageable pageable, Map<String, Object> param);
}
