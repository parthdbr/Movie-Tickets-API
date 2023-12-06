package com.movie.ticket.impl;

import com.movie.ticket.exception.*;
import com.movie.ticket.model.*;
import com.movie.ticket.repository.*;
import com.movie.ticket.DTO.*;
import com.movie.ticket.config.NullAwareBeanUtilsBean;
import com.movie.ticket.service.EmailService;
import com.movie.ticket.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    NullAwareBeanUtilsBean nullAware;

    @Autowired
    UserCriteriaRepository userCriteriaRepository;

    @Autowired
    EmailService emailService;


    @Override
    public User bookseats(@NotNull SeatsDTO seatsDTO) throws InvocationTargetException, IllegalAccessException, UserNotExistsException, CategoryNotExistsException, SeatsNotEmptyException, SeatNotAvailable {
        User user = userRepository.findByEmailContainingAndSoftDeleteIsFalse(seatsDTO.getEmail());

        Category c = categoryRepository.findByNameContainingAndSoftDeleteIsFalse(seatsDTO.getCategory());

        for(int i:seatsDTO.getBooked_seats()) {
            if(i>c.getEnd_seat_number() || i<c.getStart_seat_number()) {
                throw new SeatNotAvailable();
            }
        }

        List<Integer> selected_seats = seatsDTO.getBooked_seats();

        if (!ObjectUtils.isEmpty(c)) {

            if(!userCriteriaRepository.seatsFound(selected_seats,c.getName())) {

                if (!ObjectUtils.isEmpty(user)) {
                    nullAware.copyProperties(user, seatsDTO);
                    emailService.sendEmail(seatsDTO.getEmail(), "Your Booked Movie Tickets", "Selected seats are "+seatsDTO.getBooked_seats());
                    return userRepository.save(user);
                } else {
                    throw new UserNotExistsException();
                }

            } else {
                throw new SeatsNotEmptyException();
            }

        }else{
            throw new CategoryNotExistsException();
        }
    }
}
