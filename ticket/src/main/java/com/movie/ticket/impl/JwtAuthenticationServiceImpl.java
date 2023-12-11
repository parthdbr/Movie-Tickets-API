package com.movie.ticket.impl;

import com.movie.ticket.DTO.EmailDTO;
import com.movie.ticket.DTO.LoginDTO;
import com.movie.ticket.DTO.UserDTO;
import com.movie.ticket.RMQ.RabbitMQProducer;
import com.movie.ticket.Util.JwtUtil;
import com.movie.ticket.decorator.AuthResponse;
import com.movie.ticket.decorator.Response;
import com.movie.ticket.exception.DataAvailableException;
import com.movie.ticket.model.Email;
import com.movie.ticket.model.Role;
import com.movie.ticket.model.User;
import com.movie.ticket.repository.EmailDescRepository;
import com.movie.ticket.repository.UserRepository;
import com.movie.ticket.service.EmailService;
import com.movie.ticket.service.JwtAuthenticationService;
import com.movie.ticket.service.JwtUserDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;


@Service
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUserDetailService userDetailService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailDescRepository emailDescRepository;

    @Autowired
    RabbitMQProducer rabbitMQProducer;

    @Override
    public AuthResponse loginUser(LoginDTO loginDTO) {
        AuthResponse AuthResponse = new AuthResponse();

            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            User user = userRepository.findByEmailContainingAndSoftDeleteIsFalse(loginDTO.getUsername());

            SecurityContextHolder.getContext().setAuthentication(auth);

            if (auth.isAuthenticated()) {
                UserDetails userDetails = userDetailService.loadUserByUsername(loginDTO.getUsername());
                String token = jwtUtil.generateToken(auth);

                Map<String, Object> data = new HashMap<>();
                data.put("accessToken", token);

                AuthResponse.setStatus(new Response(HttpStatus.OK, data.get("accessToken").toString(), "200"));

            } else {

                AuthResponse.setStatus(new Response(HttpStatus.UNAUTHORIZED, "User login failed", "401"));
            }


        return AuthResponse;

    }

    @Override
    public User register(UserDTO userDTO)throws DataAvailableException {
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

            EmailDTO emailDTO = modelMapper.map(user, EmailDTO.class);
            rabbitMQProducer.sendMessage(emailDTO);
            emailDescRepository.save( modelMapper.map(emailDTO, Email.class));

            return userRepository.save(user);

        }else {
            throw new DataAvailableException("User Already exists with this Email");
        }
    }
}
