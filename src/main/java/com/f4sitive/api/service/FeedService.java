package com.f4sitive.api.service;

import com.f4sitive.api.entity.Feed;
import com.f4sitive.api.feed.model.GetFeedResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class FeedService {
    private final MongoOperations mongoOperations;
    private final MongoPersistentEntity entity;
    private final MongoEntityInformation<Feed, String> entityInformation;

    public FeedService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
        this.entity = mongoOperations.getConverter().getMappingContext().getRequiredPersistentEntity(Feed.class);
        this.entityInformation = new MappingMongoEntityInformation<>(this.entity, String.class);
    }

    public Slice<Feed> test(Pageable pageable, Map<String, Object> param) {
//        .maxTime()
//        .addCriteria()
        Sort sort = pageable.getSortOr(Sort.by(Sort.Direction.ASC, "_id"));
        Query query = new Query().with(sort);
        Optional.ofNullable(sort.getOrderFor("_id"))
                .flatMap(id -> Optional.ofNullable(param.get(id.getProperty()))
                        .map(value -> {
                            switch (id.getDirection()) {
                                case ASC:
                                    return Criteria.where(id.getProperty()).gt(new ObjectId((String) value));
                                case DESC:
                                    return Criteria.where(id.getProperty()).lt(new ObjectId((String) value));
                                default:
                                    return null;
                            }
                        }))
                .ifPresent(criteria -> {
                    AtomicReference<Criteria> reference = new AtomicReference<>(criteria);
                    List<Criteria> criteriaList = sort.stream()
                            .filter(order -> !order.getProperty().equals("_id"))
                            .map(order -> Optional.ofNullable(param.get(order.getProperty()))
                                    .map(value -> {
                                        switch (order.getDirection()) {
                                            case ASC:
                                                reference.set(reference.get().and(order.getProperty()).is(value));
                                                return Criteria.where(order.getProperty()).gt(value);
                                            case DESC:
                                                reference.set(reference.get().and(order.getProperty()).is(value));
                                                return Criteria.where(order.getProperty()).lt(value);
                                            default:
                                                return null;
                                        }
                                    }))
                            .filter(optional -> optional.isPresent())
                            .map(optional -> optional.get())
                            .collect(Collectors.toList());
                    criteriaList.add(0, reference.get());
                    query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
                });
        query.limit(pageable.getPageSize() + 1);
        List<Feed> entities = this.mongoOperations.find(query, this.entityInformation.getJavaType(), this.entityInformation.getCollectionName());
        boolean hasNext = entities.size() > pageable.getPageSize();
        List<Feed> content = new ArrayList<>(entities);
        List<String> nextParam = new ArrayList<>();
        if (hasNext) {
            content = entities.subList(0, pageable.getPageSize());
            Feed last = content.get(content.size() - 1);
            try {
                nextParam.add("_id=" + entity.getIdProperty().getRequiredGetter().invoke(last));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            sort.stream()
                    .filter(order -> !order.getProperty().equals("_id"))
                    .forEach(order -> {
                        try {
                            nextParam.add(order.getProperty() + "=" + entity.getRequiredPersistentProperty(order.getProperty()).getRequiredGetter().invoke(last));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
        }
        return new GetFeedResponse<>(content, pageable, hasNext, String.join("&", nextParam));
    }
}
