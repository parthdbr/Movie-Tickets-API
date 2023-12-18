package com.movie.ticket.service;

import com.movie.ticket.DTO.*;
import com.movie.ticket.decorator.categoryBookedSeats;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.*;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface AdminService {

    Page<User> getAllUser(UserSearchDTO userSearchDTO);

    User getUserById(String id);

    Category addCategory(CategoryDTO categoryDTO) throws DataNotAvailableException, IOException, MessagingException;

    Page<Category> getAllCategory(int page, int size);

    Category updateCategory(String id, CategoryDTO categoryDTO) throws InvocationTargetException, IllegalAccessException, DataAvailableException, IOException, MessagingException;

    void deleteCategory(String id) throws IOException, MessagingException;

    Category getCategoryByName(String name);

    categoryBookedSeats categoryBookedSeats(String id);

    User getUserBySeatsBooked(int seatNumber) throws DataNotAvailableException;

    User getUserByEmail(String email);

    Category getCategoryById(String id);

    User getUserAndAllow(String id, boolean allowed);

    User createUser(UserDTO userDTO);
}
