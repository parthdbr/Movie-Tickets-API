package com.movie.ticket.repository;

import com.movie.ticket.DTO.CategoryDTO;
import com.movie.ticket.exception.DataNotAvailableException;
import com.movie.ticket.model.Category;
import com.movie.ticket.model.User;
import com.movie.ticket.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class AdminCriteriaRepositoryImpl implements AdminCriteriaRepository {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    @Autowired
    private UserRepository userRepository;


    @Override
    public String checkSeatsAvailable(CategoryDTO categoryDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("softDelete").is(false));
        List<Category> categories = mongoTemplate.find(query,Category.class);
        String start = categoryDTO.getStart_seat_number();
        String end = categoryDTO.getEnd_seat_number();
        for(Category i : categories) {

            if ((Integer.parseInt(start) >= Integer.parseInt(i.getStart_seat_number()) && Integer.parseInt(start) <= Integer.parseInt(i.getEnd_seat_number()))
            || (Integer.parseInt(end) >=Integer.parseInt(i.getStart_seat_number()) && Integer.parseInt(end)<=Integer.parseInt(i.getEnd_seat_number()))){
                return i.getName();
            }

        }



        return null;
    }

    @Override
    public User getUserBySeatNumber(int seatNumber) throws DataNotAvailableException {

        Query query = new Query();

        query.addCriteria(Criteria.where("booked_seats").in(seatNumber));

        User user = mongoTemplate.findOne(query, User.class);

        if (user!=null)
            return user;
        else
            throw new DataNotAvailableException("Seat/Seats are available for booking");
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

        String start = categoryDTO.getStart_seat_number();
        String end = categoryDTO.getEnd_seat_number();

        for(Category i : categories) {

            if ((Integer.parseInt(start) >= Integer.parseInt(i.getStart_seat_number()) && Integer.parseInt(start) <= Integer.parseInt(i.getEnd_seat_number()))
                    || (Integer.parseInt(end)>=Integer.parseInt(i.getStart_seat_number()) && Integer.parseInt(end)<=Integer.parseInt(i.getEnd_seat_number()))){
                return i.getName();
            }

        }



        return null;
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
}
