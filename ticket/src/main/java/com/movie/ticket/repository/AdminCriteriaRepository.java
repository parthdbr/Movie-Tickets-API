package com.movie.ticket.repository;

import com.movie.ticket.exception.UserNotExistsException;
import com.movie.ticket.model.User;

public interface AdminCriteriaRepository {
    String checkSeatsAvailable(int startSeatNumber, int endSeatNumber);

    User getUserBySeatNumber(int seatNumber) throws UserNotExistsException;

    User getUserByEmail(String email);
}
