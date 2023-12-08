package com.movie.ticket.controller;


import com.movie.ticket.DTO.LoginDTO;
import com.movie.ticket.DTO.UserDTO;
import com.movie.ticket.decorator.AuthResponse;
import com.movie.ticket.decorator.DataResponse;
import com.movie.ticket.decorator.Response;
import com.movie.ticket.exception.DataAvailableException;
import com.movie.ticket.model.User;
import com.movie.ticket.service.AdminService;
import com.movie.ticket.service.JwtAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@Slf4j
public class JwtAuthenticationController {


    @Autowired
    JwtAuthenticationService jwtAuthenticationService;

    @Autowired
    AdminService adminService;

    @PostMapping("/register")
    public DataResponse<User> addUserData(@RequestBody UserDTO userDTO) throws DataAvailableException {
        DataResponse<User> response = new DataResponse<>();

        response.setData(jwtAuthenticationService.register(userDTO));

//        response.setData(adminService.addUser(userDTO));
        response.setStatus(new Response(HttpStatus.CREATED, "Data Created", "201"));

        return response;
    }

    @PostMapping(value = "/login",  produces = "application/json")
    public AuthResponse loginUser(@RequestBody LoginDTO loginDTO) {
        return jwtAuthenticationService.loginUser(loginDTO);
    }



}
