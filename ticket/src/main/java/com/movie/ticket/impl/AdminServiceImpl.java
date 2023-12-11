package com.movie.ticket.impl;

import com.movie.ticket.DTO.*;
import com.movie.ticket.decorator.categoryBookedSeats;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.*;
import com.movie.ticket.repository.*;
import com.movie.ticket.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.*;
import com.movie.ticket.service.AdminService;
import com.movie.ticket.config.NullAwareBeanUtilsBean;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AdminCriteriaRepository adminCriteriaRepository;

    @Autowired
    UserCriteriaRepository userCriteriaRepository;

    @Autowired
    NullAwareBeanUtilsBean nullAware;

    @Autowired
    EmailService emailService;

    @Override
    public Page<User> getAllUser(UserSearchDTO userSearchDTO ) {
//        Pageable pageable = PageRequest.of(page, size);
        return userCriteriaRepository.findBySoftDeleteIsFalse(userSearchDTO);
//        return userRepository.findBySoftDeleteIsFalse(pageable);
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findByIdAndSoftDeleteIsFalse(id);
    }

    @Override
    public Category addCategory(CategoryDTO categoryDTO) throws DataAvailableException, IOException, MessagingException {
        Category categoryExists = categoryRepository.findByNameContainingAndSoftDeleteIsFalse(categoryDTO.getName());
        if(ObjectUtils.isEmpty(categoryExists)) {

                Category category = modelMapper.map(categoryDTO, Category.class);
//            emailService.sendEmail(emailService.setMailData(
//                   "xyz@yopmail.com", "New Category generated", "\nName : "+category.getName()+"\nPrice : "+category.getPrice()+"\nStart : "+category.getStart_seat_number()+"\nEnd : "+category.getEnd_seat_number()));

            return categoryRepository.save(category);

        }else {
            throw new DataAvailableException("Category Already exists with this Name");
        }
    }

    @Override
    public Page<Category> getAllCategory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return categoryRepository.findBySoftDeleteIsFalse(pageable);
    }

    @Override
    public Category updateCategory(String id, CategoryDTO categoryDTO) throws InvocationTargetException, IllegalAccessException, DataAvailableException, IOException, MessagingException {
        Category category = categoryRepository.findByIdAndSoftDeleteIsFalse(id);
        if (category==null) {

            throw new DataNotAvailableException("Data Not Found with this Name");

        }
        nullAware.copyProperties(category, categoryDTO);
//        emailService.sendEmail(emailService.setMailData(
//                 "xyz@yopmail.com", "New Category generated", "\nName : "+category.getName()+"\nPrice : "+category.getPrice()+"\nStart : "+category.getStart_seat_number()+"\nEnd : "+category.getEnd_seat_number()));

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) throws IOException, MessagingException {
        Category category = categoryRepository.findByIdAndSoftDeleteIsFalse(id);
        if (!ObjectUtils.isEmpty(category)) {
            category.setSoftDelete(true);
//            emailService.sendEmail(emailService.setMailData(
//                  "xyz@yopmail.com", "New Category generated", "\nName : "+category.getName()+"\nPrice : "+category.getPrice()+"\nStart : "+category.getStart_seat_number()+"\nEnd : "+category.getEnd_seat_number()));

            categoryRepository.save(category);
        }
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByNameContainingAndSoftDeleteIsFalse(name);
    }

    @Override
    public categoryBookedSeats categoryBookedSeats(String id) {
        categoryBookedSeats result = new categoryBookedSeats();
//        result.setCategory(category.toLowerCase(Locale.ROOT));
        Category category = categoryRepository.findByIdAndSoftDeleteIsFalse(id);
        if (category!=null) {
            result.setCategory(category.getName());
            List<User> users = userRepository.findByCategoryIgnoreCaseAndSoftDeleteIsFalse(category.getName());
            List<Integer> seats = new ArrayList<>();
            for (User user : users) {
                seats.addAll(user.getBooked_seats());
            }
            result.setBooked_seats(seats);
            return result;
        }else {
            throw new DataNotAvailableException("Category Not Found");
        }

    }

    @Override
    public User getUserBySeatsBooked(int seatNumber) throws DataNotAvailableException {
        return adminCriteriaRepository.getUserBySeatNumber(seatNumber);
    }

    @Override
    public User getUserByEmail(String email) {
        return adminCriteriaRepository.getUserByEmail(email);

    }

    @Override
    public Category getCategoryById(String id) {
        return categoryRepository.findByIdAndSoftDeleteIsFalse(id);
    }

    @Override
    public User getUserAndAllow(String id, boolean allowed) {
        return adminCriteriaRepository.getUserAndAllow(id,allowed);
    }


}
