package com.movie.ticket.repository;

import com.movie.ticket.DTO.CategoryDTO;
import com.movie.ticket.exception.DataNotAvailableException;
import com.movie.ticket.model.User;

public interface AdminCriteriaRepository {
    String checkSeatsAvailable(CategoryDTO categoryDTO);

    User getUserBySeatNumber(int seatNumber) throws DataNotAvailableException;

    User getUserByEmail(String email);

    User getUserAndAllow(String id, boolean allowed);
}
