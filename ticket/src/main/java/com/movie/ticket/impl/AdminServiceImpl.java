package com.movie.ticket.impl;

import com.movie.ticket.DTO.*;
import com.movie.ticket.RMQ.RabbitMQProducer;
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
    private EmailDescRepository emailDescRepository;

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

    @Autowired
    RabbitMQProducer rabbitMQProducer;

    @Override
    public Page<User> getAllUser(UserSearchDTO userSearchDTO ) {
        return userCriteriaRepository.findBySoftDeleteIsFalse(userSearchDTO);
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

                Email<CategoryDTO> email = new Email<>();
                email.setKey("categoryCreation");
                email.setSubject("Category Created");
                email.setSomeDTO(categoryDTO);
                rabbitMQProducer.sendMessage(email);
                emailDescRepository.save(email);
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
        Email<Category> email = new Email<>();
        email.setKey("categoryUpdation");
        email.setSubject("Category Updated");
        email.setSomeDTO(category);
        rabbitMQProducer.sendMessage(email);
        emailDescRepository.save( email);
        return categoryRepository.save(category);
    }

    @Override
    public User updateUser(String id, UserDTO userDTO) throws InvocationTargetException, IllegalAccessException {
        User user = userRepository.findByIdAndSoftDeleteIsFalse(id);
        if (user == null)
            throw new DataNotAvailableException("User not found!");
        nullAware.copyProperties(user, userDTO);
        Email<UserDTO> emailDTO = new Email<>();
        emailDTO.setKey("userUpdation");
        emailDTO.setSubject("User Updated");
        emailDTO.setSomeDTO(userDTO);
        rabbitMQProducer.sendMessage(emailDTO);
        emailDescRepository.save(emailDTO);
        return userRepository.save(user);

    }


    @Override
    public void deleteCategory(String id) throws IOException, MessagingException {
        Category category = categoryRepository.findByIdAndSoftDeleteIsFalse(id);
        if (!ObjectUtils.isEmpty(category)) {
            category.setSoftDelete(true);
            Email<Category> email = new Email<>();
            email.setKey("categoryDeletion");
            email.setSubject("Category Deleted");
            email.setSomeDTO(category);
            rabbitMQProducer.sendMessage(email);
            emailDescRepository.save( email);
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

    @Override
    public User createUser(UserDTO userDTO) {
        User userExists = userRepository.findByEmailContainingAndSoftDeleteIsFalse(userDTO.getEmail());
        //validation
        if (!(userDTO.getFirst_name() != null && userDTO.getFirst_name().matches("^[a-zA-Z]*$"))){
            throw new ValidationException("First Name should contain only alphabets");
        }
        if (!(userDTO.getLast_name() != null && userDTO.getLast_name().matches("^[a-zA-Z]*$"))){
            throw new ValidationException("Last Name should contain only alphabets");
        }
        if (!(userDTO.getEmail() != null && userDTO.getEmail().matches("^(.+)@(.+)$"))){
            throw new ValidationException("Email Name should contain @ followed by .");
        }
        if (!ObjectUtils.isEmpty(userExists)) {
            throw new DataAvailableException("User Already exists with this Email");
        }

        //Implementation
        User user = modelMapper.map(userDTO, User.class);

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of(Role.USER));
        } else if (user.getRoles().contains(Role.USER)) {
            user.setRoles(List.of(Role.USER));
        } else if (user.getRoles().contains(Role.ADMIN)) {
            user.setRoles(List.of(Role.ADMIN));
        } else if (new HashSet<>(user.getRoles()).containsAll(List.of(Role.ADMIN, Role.USER))){
            user.setRoles(Arrays.asList(Role.ADMIN, Role.USER));
        }
        Email<UserDTO> emailDTO = new Email<>();
        emailDTO.setKey("userRegistration");
        emailDTO.setSubject("New User Registered");
        emailDTO.setSomeDTO(userDTO);
        rabbitMQProducer.sendMessage(emailDTO);
        emailDescRepository.save(emailDTO);


        return userRepository.save(user);
    }



}
