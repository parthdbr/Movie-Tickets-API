package com.movie.ticket.service;

import com.movie.ticket.DTO.SeatsDTO;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.User;

import java.lang.reflect.InvocationTargetException;

public interface UserService {

    User bookseats(SeatsDTO seatsDTO) throws InvocationTargetException, IllegalAccessException, UserNotExistsException, CategoryNotExistsException, SeatsNotEmptyException, SeatNotAvailable;
}
