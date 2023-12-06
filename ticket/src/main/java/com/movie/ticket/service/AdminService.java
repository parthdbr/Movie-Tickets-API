package com.movie.ticket.service;

import com.movie.ticket.DTO.*;
import com.movie.ticket.decorator.categoryBookedSeats;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.*;
import org.springframework.data.domain.Page;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public interface AdminService {
    public User addUser(UserDTO userDTO) throws UserExistsException;

    Page<User> getAllUser(int page, int size);

    Category addCategory(CategoryDTO categoryDTO) throws CategoryExistsException, SeatNotAvailable;

    List<Category> getAllCategory();

    Category updateCategory(String name, CategoryDTO categoryDTO) throws InvocationTargetException, IllegalAccessException;

    void deleteCategory(String name);

    Category getCategoryByName(String name);

    categoryBookedSeats categoryBookedSeats(String category);

    User getUserBySeatsBooked(int seatNumber) throws UserNotExistsException;
}
