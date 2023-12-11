package com.movie.ticket.impl;

import com.movie.ticket.RMQ.RabbitMQProducer;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.*;
import com.movie.ticket.repository.*;
import com.movie.ticket.DTO.*;
import com.movie.ticket.config.NullAwareBeanUtilsBean;
import com.movie.ticket.service.EmailService;
import com.movie.ticket.service.UserService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


@Service
@Slf4j
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

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public User bookseats(@NotNull SeatsDTO seatsDTO) throws InvocationTargetException, IllegalAccessException, DataNotAvailableException, IOException, MessagingException {
        User user = userRepository.findByEmailContainingAndSoftDeleteIsFalse(seatsDTO.getEmail());

        Category c = categoryRepository.findByNameContainingAndSoftDeleteIsFalse(seatsDTO.getCategory());

        for(int i:seatsDTO.getBooked_seats()) {
            if(i>Integer.parseInt(c.getEnd_seat_number()) || i<Integer.parseInt(c.getStart_seat_number())) {
                throw new DataNotAvailableException("Seat/Seats are not available");
            }
        }

        List<Integer> selected_seats = seatsDTO.getBooked_seats();

        if (!ObjectUtils.isEmpty(c)) {

            if(!userCriteriaRepository.seatsFound(selected_seats,c.getName())) {

                if (!ObjectUtils.isEmpty(user)) {
                    nullAware.copyProperties(user, seatsDTO);

                    User user1 = userRepository.findByEmailContainingAndSoftDeleteIsFalse(seatsDTO.getEmail());
                    EmailDTO emailDTO = modelMapper.map(user1, EmailDTO.class);

//                    emailService.sendEmail(emailDTO);

//                    rabbitMQProducer.sendMessage(emailDTO);
                    return userRepository.save(user);
                } else {
                    throw new DataNotAvailableException("User Does not exists");
                }

            } else {
                throw new DataNotAvailableException("Seat/Seats are not available");
            }

        }else{
            throw new DataNotAvailableException("Category does not exists");
        }
    }
}
