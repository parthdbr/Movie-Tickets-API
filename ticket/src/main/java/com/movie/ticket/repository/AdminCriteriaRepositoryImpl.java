package com.movie.ticket.repository;

import com.movie.ticket.DTO.CategoryDTO;
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
    public String checkSeatsAvailable(CategoryDTO categoryDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("softDelete").is(false));
        List<Category> categories = mongoTemplate.find(query,Category.class);
        int start = categoryDTO.getStart_seat_number();
        int end = categoryDTO.getEnd_seat_number();
        for(Category i : categories) {

            if ((start >= i.getStart_seat_number() && start <= i.getEnd_seat_number())
            || (end>=i.getStart_seat_number() && end<=i.getEnd_seat_number())){
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

    @Override
    public User getUserByEmail(String email) {
        Query query = new Query();

        query.addCriteria(Criteria.where("email").is(email).and("softDelete").ne(true));

        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public String checkSeatsAvailabletoUpdate(CategoryDTO categoryDTO, String id) {

        Query query = new Query();

        query.addCriteria(Criteria.where("id").ne(id).and("softDelete").is(false));


        List<Category> categories = mongoTemplate.find(query, Category.class);

        int start = categoryDTO.getStart_seat_number();
        int end = categoryDTO.getEnd_seat_number();

        for(Category i : categories) {

            if ((start >= i.getStart_seat_number() && start <= i.getEnd_seat_number())
                    || (end>=i.getStart_seat_number() && end<=i.getEnd_seat_number())){
                return i.getName();
            }

        }



        return null;
    }
}
