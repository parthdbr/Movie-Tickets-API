package com.movie.ticket.controller;

import com.movie.ticket.Annotation.Access;
import com.movie.ticket.model.Role;
import com.movie.ticket.model.User;
import com.movie.ticket.repository.StreamRepository;
import com.movie.ticket.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/streams")
@SecurityRequirement(name = "BearerAuth")
public class streamsController {

    @Autowired
    StreamRepository streamRepository;

    @GetMapping("/city_wise")
    @Access(roles = Role.ADMIN)
    public Map<String, List<User>> city_wise(){
        return streamRepository.findUsersCityWise();

    }

    @GetMapping("/state_city_wise")
    @Access(roles = Role.ADMIN)
    public Map<Pair<String,String>, List<User>> state_city_wise(){
        return streamRepository.findUsersStateAndCityWise();

    }

    @GetMapping("/country_state_city_wise")
    @Access(roles = Role.ADMIN)
    public Map<Pair<String, Pair<String,String>>, List<User>> country_state_city_wise(){
        return streamRepository.findUsersCountryAndStateAndCityWise();

    }

    @GetMapping("/sort_by_birthdate")
    @Access(roles = Role.ADMIN)
    public Map<String, List<User>> sort_by_birthdate(){
        return streamRepository.sortUsersByBirthdate();

    }

    @GetMapping("/collect_by_Email")
    @Access(roles = Role.ADMIN)
    public Map<String, List<User>> collectByEmail(){
        return streamRepository.collectByEmail();

    }

    @GetMapping("/adult_users")
    @Access(roles = Role.ADMIN)
    public Map<String, List<User>> adultUsers(){
        return streamRepository.adultUsers();

    }

    @GetMapping("/unique_city_names")
    @Access(roles = Role.ADMIN)
    public Set<String> cityNames(){
        return streamRepository.uniqueCityNames();

    }



}
