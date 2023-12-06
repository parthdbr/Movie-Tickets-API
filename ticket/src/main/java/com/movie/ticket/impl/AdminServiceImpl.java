package com.movie.ticket.impl;

import com.movie.ticket.DTO.*;
import com.movie.ticket.decorator.categoryBookedSeats;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.*;
import com.movie.ticket.repository.*;
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
    @Override
    public User addUser(@NotNull UserDTO userDTO) throws UserExistsException {
        User userExists = userRepository.findByEmailContainingAndSoftDeleteIsFalse(userDTO.getEmail());

        if (ObjectUtils.isEmpty(userExists)) {

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

            return userRepository.save(user);

        }else {
            log.info("User Exists");
            throw new UserExistsException("User Already exists with this Email");
        }
    }

    @Override
    public Page<User> getAllUser(UserSearchDTO userSearchDTO ) {
//        Pageable pageable = PageRequest.of(page, size);
        return userCriteriaRepository.findBySoftDeleteIsFalse(userSearchDTO);
//        return userRepository.findBySoftDeleteIsFalse(pageable);
    }

    @Override
    public Category addCategory(CategoryDTO categoryDTO) throws CategoryExistsException, SeatNotAvailable {
        Category categoryExists = categoryRepository.findByNameContainingAndSoftDeleteIsFalse(categoryDTO.getName());
        if(ObjectUtils.isEmpty(categoryExists)) {

                Category category = modelMapper.map(categoryDTO, Category.class);
                return categoryRepository.save(category);

        }else {
            log.info("Category Exists");
            throw new CategoryExistsException("Category Already exists with this Name");
        }
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findBySoftDeleteIsFalse();
    }

    @Override
    public Category updateCategory(String id, CategoryDTO categoryDTO) throws InvocationTargetException, IllegalAccessException {
        Category category = categoryRepository.findByIdAndSoftDeleteIsFalse(id);
        nullAware.copyProperties(category, categoryDTO);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) {
        Category category = categoryRepository.findByIdAndSoftDeleteIsFalse(id);
        if (!ObjectUtils.isEmpty(category)) {
            category.setSoftDelete(true);
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
        result.setCategory(category.getName());
        List<User> users = userRepository.findByCategoryIgnoreCaseAndSoftDeleteIsFalse(category.getName());
        List<Integer> seats = new ArrayList<>();
        for (User user : users) {
            seats.addAll(user.getBooked_seats());
        }
        result.setBooked_seats(seats);
        return result;
    }

    @Override
    public User getUserBySeatsBooked(int seatNumber) throws UserNotExistsException {
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


}
