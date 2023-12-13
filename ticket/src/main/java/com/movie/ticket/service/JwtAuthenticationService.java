package com.movie.ticket.service;

import com.movie.ticket.DTO.LoginDTO;
import com.movie.ticket.DTO.UserDTO;
import com.movie.ticket.decorator.AuthResponse;
import com.movie.ticket.exception.DataAvailableException;
import com.movie.ticket.model.User;

import java.util.concurrent.ExecutionException;

public interface JwtAuthenticationService {
    AuthResponse loginUser(LoginDTO loginDTO);

    AuthResponse validate(String username, int otp) throws ExecutionException;

    User register(UserDTO userDTO) throws DataAvailableException;
}
