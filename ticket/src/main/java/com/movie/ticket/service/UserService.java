package com.movie.ticket.service;

import com.movie.ticket.DTO.SeatsDTO;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.User;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface UserService {

    User bookseats(SeatsDTO seatsDTO) throws InvocationTargetException, IllegalAccessException, DataNotAvailableException, IOException, MessagingException;
}
