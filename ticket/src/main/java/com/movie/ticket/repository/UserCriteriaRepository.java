package com.movie.ticket.repository;

import com.movie.ticket.DTO.UserSearchDTO;
import com.movie.ticket.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserCriteriaRepository {
    boolean seatsFound(List<Integer> bookSeats, String categoryName);

    Page<User> findBySoftDeleteIsFalse(UserSearchDTO userSearchDTO);
}
