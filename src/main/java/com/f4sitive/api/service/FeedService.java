package com.f4sitive.api.service;

import com.f4sitive.api.entity.Feed;
import com.f4sitive.api.entity.User;
import com.f4sitive.api.model.Slice;
import com.f4sitive.api.repository.CategoryRepository;
import com.f4sitive.api.repository.FeedRepository;
import com.f4sitive.api.repository.MissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FeedService {
    private final MongoOperations mongoOperations;
    private final MongoPersistentEntity entity;
    private final MongoEntityInformation<Feed, String> entityInformation;
    private final FeedRepository feedRepository;
    private final MissionRepository missionRepository;
    private final CategoryRepository categoryRepository;

    public FeedService(MongoOperations mongoOperations, FeedRepository feedRepository, MissionRepository missionRepository, CategoryRepository categoryRepository) {
        this.mongoOperations = mongoOperations;
        this.entity = mongoOperations.getConverter().getMappingContext().getRequiredPersistentEntity(Feed.class);
        this.categoryRepository = categoryRepository;
        this.entityInformation = new MappingMongoEntityInformation<>(this.entity, String.class);
        this.feedRepository = feedRepository;
        this.missionRepository = missionRepository;
    }

    public Mono<Feed> findById(String id) {
//        log.info("{}", feedRepository.findAllByCategoryId(id, Pageable.unpaged()).getContent());
        return Mono.defer(() -> Mono.justOrEmpty(feedRepository.findById(id)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Void> deleteById(String userId, String id){
        return Mono.defer(() -> Mono.justOrEmpty(feedRepository.findById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)))
                .filter(feed -> feed.getUser().getId().equals(userId))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN)))
                .flatMap(feed -> {
                    feedRepository.delete(feed);
                    return Mono.<Void>empty();
                }))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Feed> saveById(String id, Function<Feed, Feed> function){
        return Mono.defer(() -> Mono.justOrEmpty(feedRepository.findById(id).map(function).map(feedRepository::save)))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Feed> save(String userId, String missionId, Feed entity) {
        return Mono.defer(() -> Mono.justOrEmpty(missionRepository.findById(missionId)
                .map(mission -> {
                    entity.setUser(User.id(userId));
                    entity.setMission(mission);
                    entity.setCategory(mission.getCategory());
                    return feedRepository.save(entity);
                })
                .map(feed -> {
                    feed.getMission().getFeeds().add(feed);
                    missionRepository.save(feed.getMission());
                    feed.getCategory().getFeeds().add(feed);
                    categoryRepository.save(feed.getCategory());
                    return feed;
                })
        ))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Slice<Feed>> findAll(Pageable pageable, String token, Optional<Criteria>... optionals){
        return Mono.fromCallable(() -> query(pageable, token, optionals))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Slice<Feed> query(Pageable pageable, String token, Optional<Criteria>... optionals) {
        Map<String, String> param = decode(token);
        Sort sort = pageable.getSortOr(Sort.by(Sort.Direction.DESC, "id"));
        Query query = new Query().with(sort);
        Arrays.stream(optionals).filter(Optional::isPresent).map(Optional::get).forEach(query::addCriteria);
        Optional.ofNullable(sort.getOrderFor("id"))
                .flatMap(id -> Optional.ofNullable(param.get(id.getProperty()))
                        .map(value -> {
                            switch (id.getDirection()) {
                                case ASC:
                                    return Criteria.where(id.getProperty()).gt(new ObjectId(value));
                                case DESC:
                                    return Criteria.where(id.getProperty()).lt(new ObjectId(value));
                                default:
                                    return null;
                            }
                        }))
                .ifPresent(criteria -> {
                    AtomicReference<Criteria> reference = new AtomicReference<>(criteria);
                    List<Criteria> criteriaList = sort.stream()
                            .filter(order -> !order.getProperty().equals("id"))
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
        List<Feed> entities = this.mongoOperations.find(query, Feed.class);
        boolean hasNext = entities.size() > pageable.getPageSize();
        List<Feed> content = new ArrayList<>(entities);
        Map<String, String> nextParam = new LinkedHashMap<>();
        if (hasNext) {
            content = entities.subList(0, pageable.getPageSize());
            Feed last = content.get(content.size() - 1);
            try {
                nextParam.put("id", (String) entity.getIdProperty().getRequiredGetter().invoke(last));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            sort.stream()
                    .filter(order -> !order.getProperty().equals("id"))
                    .forEach(order -> {
                        try {
                            nextParam.put(order.getProperty(), (String) entity.getRequiredPersistentProperty(order.getProperty()).getRequiredGetter().invoke(last));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
        }
        return new Slice<>(content, encode(nextParam));
    }

    private Map<String, String> decode(String src){
        return Optional.ofNullable(src)
                .map(Base64Utils::decodeFromUrlSafeString)
                .map(String::new)
                .map(token -> token.split("&"))
                .map(Arrays::asList)
                .orElse(Collections.emptyList())
                .stream()
                .map(name -> name.split("="))
                .filter(names -> names.length == 2)
                .collect(LinkedHashMap::new,
                        (map, names) -> map.put(names[0], names[1]),
                        Map::putAll);
    }
    private String encode(Map<String, String> src){
        return Base64Utils.encodeToUrlSafeString(src
                .entrySet()
                .stream()
                .map(entity -> entity.getKey() + "=" + entity.getValue())
                .collect(Collectors.joining("&"))
                .getBytes());
    }
}
