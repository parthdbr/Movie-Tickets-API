package com.movie.ticket.repository;

import com.movie.ticket.DTO.AgeDTO;
import com.movie.ticket.DTO.CategoryDTO;
import com.movie.ticket.exception.DataNotAvailableException;
import com.movie.ticket.model.Category;
import com.movie.ticket.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
@Slf4j
public class AdminCriteriaRepositoryImpl implements AdminCriteriaRepository {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    @Autowired
    private UserRepository userRepository;


    @Override
    public String checkSeatsAvailable(CategoryDTO categoryDTO) {


        int start = Integer.parseInt(categoryDTO.getStart_seat_number());
        int end = Integer.parseInt(categoryDTO.getEnd_seat_number());

        List<Category> categories = mongoTemplate.findAll(Category.class);

        return categories.stream()
                .filter(category -> !category.isSoftDelete())
                .filter(category ->
                        (start >= Integer.parseInt(category.getStart_seat_number()) && start <= Integer.parseInt(category.getEnd_seat_number())) ||
                                (end >= Integer.parseInt(category.getStart_seat_number()) && end <= Integer.parseInt(category.getEnd_seat_number())))
                .map(Category::getName)
                .findFirst()
                .orElse(null);

    }

    @Override
    public User getUserBySeatNumber(int seatNumber) throws DataNotAvailableException {
/*
        Aggregation aggregation = Aggregation.newAggregation(
          Aggregation.match(Criteria.where("booked_seats").in(seatNumber))
        );

        AggregationResults<User> results = mongoTemplate.aggregate(aggregation, "user", User.class);*/

//        log.info("--> {}", results.getMappedResults());

        Query query = new Query();

        query.addCriteria(Criteria.where("booked_seats").in(seatNumber));

        User user = mongoTemplate.findOne(query, User.class);


        if (!ObjectUtils.isEmpty(user))
            return user;
        else
            throw new DataNotAvailableException("Seat/Seats are available for booking");
    }

    @Override
    public User getUserByEmail(String email) {

        List<User> userList = mongoTemplate.findAll(User.class);

       /* Aggregation aggregation = Aggregation.newAggregation(
          Aggregation.match(Criteria.where("email").is(email).and("softDelete").is(false))
        );

        AggregationResults<User> results = mongoTemplate.aggregate(aggregation, "user", User.class);

       return results.getMappedResults().stream().findFirst().orElse(null);*/

        return (mongoTemplate.findAll(User.class)).stream()
                .filter(p -> p.getEmail().equals(email))
                .filter(p -> !p.isSoftDelete()).findAny().orElse(null);

    }


    @Override
    public User getUserAndAllow(String id, boolean allowed) {
        User user = userRepository.findByIdAndSoftDeleteIsFalse(id);

        if (!ObjectUtils.isEmpty(user)) {
            user.setActive(allowed);
            return userRepository.save(user);
        }else{
            return null;
        }
    }

    @Override
    public List<?> findAgeOfUser() {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("birthdate").ne(null)
                        .and("first_name").ne(null)
                        .and("last_name").ne(null)),
                addFields().addField("full_name").withValue(StringOperators.Concat.valueOf("$first_name").concat(" ").concatValueOf("$last_name"))
                        .addField("age").withValue(ArithmeticOperators.Subtract.valueOf(DateOperators.Year.year(new Date())).subtract(DateOperators.Year.yearOf("$birthdate"))).build(),
                sort(Sort.Direction.ASC, "age"),
                match(Criteria.where("age").gte(18))
        );
        return mongoTemplate.aggregate(aggregation,"user", AgeDTO.class).getMappedResults();
    }
}
