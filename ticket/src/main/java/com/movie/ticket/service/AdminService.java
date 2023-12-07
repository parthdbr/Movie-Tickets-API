package com.movie.ticket.service;

import com.movie.ticket.DTO.*;
import com.movie.ticket.decorator.categoryBookedSeats;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.*;
import org.springframework.data.domain.Page;

import java.lang.reflect.InvocationTargetException;

public interface AdminService {
    public User addUser(UserDTO userDTO) throws DataAvailableException;

    Page<User> getAllUser(UserSearchDTO userSearchDTO);

    Category addCategory(CategoryDTO categoryDTO) throws DataNotAvailableException;

    Page<Category> getAllCategory(int page, int size);

    Category updateCategory(String id, CategoryDTO categoryDTO) throws InvocationTargetException, IllegalAccessException, DataAvailableException;

    void deleteCategory(String id);

    Category getCategoryByName(String name);

    categoryBookedSeats categoryBookedSeats(String id);

    User getUserBySeatsBooked(int seatNumber) throws DataNotAvailableException;

    User getUserByEmail(String email);

    Category getCategoryById(String id);
}
