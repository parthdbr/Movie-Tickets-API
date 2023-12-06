package com.movie.ticket.repository;

import com.movie.ticket.exception.UserNotExistsException;
import com.movie.ticket.model.Category;
import com.movie.ticket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

@Component
public class AdminCriteriaRepositoryImpl implements AdminCriteriaRepository {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String checkSeatsAvailable(int startSeatNumber, int endSeatNumber) {
        List<Category> categories = categoryRepository.findBySoftDeleteIsFalse();

        for(Category i : categories) {

            if ((startSeatNumber >= i.getStart_seat_number() && startSeatNumber <= i.getEnd_seat_number())
            || (endSeatNumber>=i.getStart_seat_number() && endSeatNumber<=i.getEnd_seat_number())){
                return i.getName();
            }

        }



        return null;
    }

    @Override
    public User getUserBySeatNumber(int seatNumber) throws UserNotExistsException {

        Query query = new Query();

        query.addCriteria(Criteria.where("booked_seats").in(seatNumber));

        User user = mongoTemplate.findOne(query, User.class);

        if (user!=null)
            return user;
        else
            throw new UserNotExistsException();
    }
}
