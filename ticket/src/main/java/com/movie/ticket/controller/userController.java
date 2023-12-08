package com.movie.ticket.controller;

import com.movie.ticket.DTO.SeatsDTO;
import com.movie.ticket.decorator.DataResponse;
import com.movie.ticket.decorator.Response;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.User;
import com.movie.ticket.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "BearerAuth")
public class userController {

    @Autowired
    UserService userService;

    @PostMapping("/book_seats")
    public DataResponse<User> bookSeats(@RequestBody SeatsDTO seatsDTO) throws DataAvailableException, InvocationTargetException, IllegalAccessException, IOException, MessagingException {
        DataResponse<User> response = new DataResponse<>();


            if(!ObjectUtils.isEmpty(seatsDTO)) {
                response.setData(userService.bookseats(seatsDTO));

                response.setStatus(new Response(HttpStatus.ACCEPTED, "Seats booked", "202"));
            }else
                response.setStatus(new Response(HttpStatus.NOT_ACCEPTABLE, "Please enter the required data", "406"));

        return response;
    }
}
