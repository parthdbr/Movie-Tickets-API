package com.movie.ticket.controller;

import com.movie.ticket.Annotation.Access;
import com.movie.ticket.model.Role;
import com.movie.ticket.model.User;
import com.movie.ticket.repository.StreamRepository;
import com.movie.ticket.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/streams")
@SecurityRequirement(name = "BearerAuth")
public class streamsController {

    @Autowired
    StreamRepository streamRepository;

    @GetMapping("/city_wise")
    @Access(roles = Role.ADMIN)
    public Map<?, ?> city_wise(){
        return streamRepository.findUsersCityWise();

    }

    @GetMapping("/state_city_wise")
    @Access(roles = Role.ADMIN)
    public Map<?, ?> state_city_wise(){
        return streamRepository.findUsersStateAndCityWise();

    }

    @GetMapping("/country_state_city_wise")
    @Access(roles = Role.ADMIN)
    public Map<?, ?> country_state_city_wise(){
        return streamRepository.findUsersCountryAndStateAndCityWise();

    }

    @GetMapping("/sort_by_birthdate")
    @Access(roles = Role.ADMIN)
    public Map<?, ?> sort_by_birthdate(){
        return streamRepository.sortUsersByBirthdate();

    }


}
