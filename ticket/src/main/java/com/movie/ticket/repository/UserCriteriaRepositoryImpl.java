package com.movie.ticket.repository;

import com.movie.ticket.DTO.UserSearchDTO;
import com.movie.ticket.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserCriteriaRepositoryImpl implements UserCriteriaRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    MongoOperations mongoOperations;


    @Override
    public boolean seatsFound(List<Integer> bookSeats,String categoryName) {

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("booked_seats").in(bookSeats).and("category").is(categoryName))
        );

        AggregationResults<?> results = mongoTemplate.aggregate(aggregation, "user", User.class);

        return !results.getMappedResults().isEmpty();
/*
        Query query = new Query();


        Criteria criteria = new Criteria();

        criteria.andOperator(
                Criteria.where("booked_seats").in(bookSeats),
                Criteria.where("category").is(categoryName)
        );


        query.addCriteria(criteria);

        List<User> user = mongoTemplate.find(query, User.class);

        return !user.isEmpty();
*/
    }

    @Override
    public Page<User> findBySoftDeleteIsFalse(UserSearchDTO data) {
        Pageable pageable;
        pageable = PageRequest.of(data.getPage(), data.getSize());

        Query query = new Query().with(pageable);

        Criteria criteria = new Criteria();

        if (data.getSearch() != null) {

            criteria.andOperator(
                    Criteria.where("softDelete").is(false),
                    new Criteria().orOperator(
                            Criteria.where("first_name").regex(".*" + data.getSearch() + ".*", "i"),
                            Criteria.where("last_name").regex(".*" + data.getSearch() + ".*", "i"),
                            Criteria.where("email").regex(".*" + data.getSearch() + ".*", "i")

                    ));
        }else{
            criteria.andOperator(Criteria.where("softDelete").is(false));
        }

        if (data.getCategory() != null) {
            query.addCriteria(Criteria.where("category").is(data.getCategory().toLowerCase()));
        }

        if (data.getOrder() != null && data.getField() != null) {
            query.addCriteria(criteria).with(Sort.by(Sort.Direction.valueOf(data.getOrder()), data.getField()));
        }else{
            query.addCriteria(criteria);
        }

        List<User> res = mongoTemplate.find(query, User.class);

        return new PageImpl<>(res, pageable, mongoOperations.count(query, User.class));


    }
}
