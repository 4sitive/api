package com.f4sitive.api.repository;

import com.f4sitive.api.entity.CursorImpl;
import com.f4sitive.api.entity.Test;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;

import java.util.ArrayList;
import java.util.List;

public class TestRepositoryImpl implements TestRepositoryCustom {
    private final MongoOperations mongoOperations;
    private final MongoPersistentEntity entity;
    private final MongoEntityInformation<Test, String> entityInformation;

    public TestRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
        this.entity = mongoOperations.getConverter().getMappingContext().getRequiredPersistentEntity(Test.class);
        this.entityInformation = new MappingMongoEntityInformation<>(entity, String.class);
    }

    @Override
    public void test(Pageable pageable) {
//        .maxTime()
//        .addCriteria()
        Query query = new Query().with(pageable.getSort());
//        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "_id")).ascending()
//        pageable.getSort().getOrderFor()
        switch (pageable.getSortOr(Sort.by(Sort.Direction.ASC, "_id")).getOrderFor("_id").getDirection()) {
            case ASC:
                query.addCriteria(new Criteria().orOperator(Criteria.where("_id").is("").and("_id").gt(new ObjectId("")), Criteria.where("_id").gt("")));
                break;
            case DESC:
                query.addCriteria(new Criteria().orOperator(Criteria.where("_id").is("").and("_id").lt(new ObjectId("")), Criteria.where("_id").lt("")));
                break;
            default:
        }
        query.limit(pageable.getPageSize() + 1);

        List<Test> entities = this.mongoOperations.find(query, this.entityInformation.getJavaType(), this.entityInformation.getCollectionName());
        boolean hasNext = entities.size() > pageable.getPageSize();
        String continuationToken = null;
        List<Test> toReturn = new ArrayList<>(entities);

        if (hasNext) {
//            entity.getIdProperty().in
//            toReturn = entities.subList(0, pageable.getSize());
//            Test last = toReturn.get(toReturn.size() - 1);
//            String plainToken = hashed + "_" + getValue(persistentEntity.getIdProperty(), last);
//            if (isSorted) {
//                Object sortValue = getValue(persistentEntity.getPersistentProperty(pageRequest.getSort()), last);
//                plainToken += "_" + pageRequest.getSort() + "_" + sortValue;
//            }
//            log.debug("Plain continuationToken: {}", plainToken);
//            continuationToken = encrypt(plainToken);
        }
        new CursorImpl<Test>(entities, pageable, hasNext, "");
    }
}
