package com.movie.ticket.repository;

import com.movie.ticket.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminCriteriaRepositoryImpl implements AdminCriteriaRepository {

    @Autowired
    CategoryRepository categoryRepository;

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
}
