package com.movie.ticket.repository;

import com.movie.ticket.DTO.CategoryDTO;
import com.movie.ticket.exception.UserNotExistsException;
import com.movie.ticket.model.User;

public interface AdminCriteriaRepository {
    String checkSeatsAvailable(CategoryDTO categoryDTO);

    User getUserBySeatNumber(int seatNumber) throws UserNotExistsException;

    User getUserByEmail(String email);

    String checkSeatsAvailabletoUpdate(CategoryDTO categoryDTO, String id);
}
