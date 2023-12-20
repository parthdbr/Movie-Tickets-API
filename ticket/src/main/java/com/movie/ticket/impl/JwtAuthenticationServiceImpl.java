package com.movie.ticket.impl;

import com.google.common.cache.LoadingCache;
import com.movie.ticket.DTO.EmailDTO;
import static java.util.concurrent.TimeUnit.*;
import com.movie.ticket.DTO.LoginDTO;
import com.movie.ticket.DTO.UserDTO;
import com.movie.ticket.JWT.JwtUser;
import com.movie.ticket.RMQ.RabbitMQProducer;
import com.movie.ticket.Util.JwtUtil;
import com.movie.ticket.decorator.AuthResponse;
import com.movie.ticket.decorator.Response;
import com.movie.ticket.exception.DataAvailableException;
import com.movie.ticket.exception.ValidationException;
import com.movie.ticket.model.*;
import com.movie.ticket.repository.EmailDescRepository;
import com.movie.ticket.repository.UserRepository;
import com.movie.ticket.service.JwtAuthenticationService;
import com.movie.ticket.service.JwtUserDetailService;
import com.movie.ticket.service.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Service
@Slf4j
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

    @Autowired
    OtpService otpService;

    long MAX_DURATION = MILLISECONDS.convert(3, MINUTES);

    private Map<String, String> authe = new HashMap<>();

    @Override
    public AuthResponse loginUser(LoginDTO loginDTO) {

        AuthResponse AuthResponse = new AuthResponse();

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        User user = userRepository.findByEmailContainingAndSoftDeleteIsFalse(loginDTO.getUsername());

        SecurityContextHolder.getContext().setAuthentication(auth);

        if (auth.isAuthenticated()) {
            if (user.isActive()) {
                authe.put(loginDTO.getUsername(), loginDTO.getPassword());

                OTP otp = new OTP();
                int Otp = otpService.genereateOtp(loginDTO.getUsername());
                otp.setOtp(Otp);
                otp.setUsername(loginDTO.getUsername());
                Email<OTP> email = new Email<>();
                email.setKey("otpGeneration");
                email.setSubject("OTP Generated");
                email.setSomeDTO(otp);
                rabbitMQProducer.sendMessage(email);
                emailDescRepository.save(email);
                user.setOtp(Otp);
                user.setGenerateOtpTime(new Date());
                user.setOtpCount(0);
                userRepository.save(user);
                AuthResponse.setStatus(new Response(HttpStatus.OK, "OTP Sent", "200"));
            }else{
                AuthResponse.setStatus(new Response(HttpStatus.UNAUTHORIZED, "Your account is blocked", "401"));
            }

        } else {

                AuthResponse.setStatus(new Response(HttpStatus.UNAUTHORIZED, "User login failed", "401"));
        }


        return AuthResponse;

    }

    @Override
    public AuthResponse validate(String username, int otp) throws ExecutionException {

        Date compareDate = new Date();

        AuthResponse AuthResponse = new AuthResponse();

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authe.get(username)));
//        log.info("Authentication {}", auth.getName());
//        log.info("Authentication {}", auth.getAuthorities());
        User user = userRepository.findByEmailContainingAndSoftDeleteIsFalse(username);

        SecurityContextHolder.getContext().setAuthentication(auth);


        if (auth.isAuthenticated()) {
            if (user.isActive()) {
                if (otp == user.getOtp()) {
                    if (compareDate.getTime() - user.getGenerateOtpTime().getTime() <= MAX_DURATION) {
//                        UserDetails userDetails = userDetailService.loadUserByUsername(username);

                        String authorities =  auth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(","));


                        String token;
                        token = jwtUtil.generateToken(new JwtUser(auth.getName(), null, authorities), user.getId());

                        Map<String, Object> data = new HashMap<>();
                        data.put("accessToken", token);

                        AuthResponse.setStatus(new Response(HttpStatus.OK, data.get("accessToken").toString(), "200"));
                    } else {
                        AuthResponse.setStatus(new Response(HttpStatus.UNAUTHORIZED, "OTP Expired", "401"));

                    }
                } else {

                    if (user.getOtpCount() <= 3) {
                        AuthResponse.setStatus(new Response(HttpStatus.UNAUTHORIZED, "OTP is incorrect", "401"));
                        user.setOtpCount(user.getOtpCount() + 1);
                        userRepository.save(user);
                    } else {
                        AuthResponse.setStatus(new Response(HttpStatus.UNAUTHORIZED, "Your account is blocked", "401"));
                        user.setActive(false);
                        userRepository.save(user);
                    }
                }
            }else{
                AuthResponse.setStatus(new Response(HttpStatus.UNAUTHORIZED, "Your account is blocked", "401"));
            }
        } else {

            AuthResponse.setStatus(new Response(HttpStatus.UNAUTHORIZED, "User login failed", "401"));
        }
        return AuthResponse;

    }

    @Override
    public User register(UserDTO userDTO)throws DataAvailableException {
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
