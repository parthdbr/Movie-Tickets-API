package com.movie.ticket.repository;

import com.movie.ticket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserCriteriaRepositoryImpl implements UserCriteriaRepository {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public boolean seatsFound(List<Integer> bookSeats,String categoryName) {

        Query query = new Query();


        Criteria criteria = new Criteria();

        criteria.andOperator(
                Criteria.where("booked_seats").in(bookSeats),
                Criteria.where("category").is(categoryName)
        );


        query.addCriteria(criteria);

        List<User> user = mongoTemplate.find(query, User.class);

        return !user.isEmpty();
    }
}
