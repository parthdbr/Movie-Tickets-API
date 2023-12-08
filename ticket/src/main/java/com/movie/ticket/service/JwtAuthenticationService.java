package com.movie.ticket.service;

import com.movie.ticket.DTO.LoginDTO;
import com.movie.ticket.DTO.UserDTO;
import com.movie.ticket.decorator.AuthResponse;
import com.movie.ticket.exception.DataAvailableException;
import com.movie.ticket.model.User;

public interface JwtAuthenticationService {
    AuthResponse loginUser(LoginDTO loginDTO);

    User register(UserDTO userDTO) throws DataAvailableException;
}
