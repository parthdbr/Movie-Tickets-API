package com.movie.ticket.repository;

import com.movie.ticket.model.User;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface StreamRepository {
    Map<String, List<User>> findUsersCityWise();

    Map<Pair<String,String>, List<User>> findUsersStateAndCityWise();

    Map<Pair<String, Pair<String,String>>, List<User>> findUsersCountryAndStateAndCityWise();

    Map<String, List<User>> sortUsersByBirthdate();

    Map<String, List<User>> collectByEmail();

    Map<String, List<User>> adultUsers();

    Map<String, Boolean> uniqueCityNames();
}
