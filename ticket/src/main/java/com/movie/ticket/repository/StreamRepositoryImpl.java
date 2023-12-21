package com.movie.ticket.repository;

import com.movie.ticket.model.User;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StreamRepositoryImpl implements StreamRepository{

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Map<String, List<User>> findUsersCityWise() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .collect(Collectors.groupingBy(User::getCity));

    }

    @Override
    public Map<Pair<String,String>, List<User>> findUsersStateAndCityWise() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .collect(Collectors.groupingBy(user ->
                        new Pair<>(user.getState(), user.getCity()),
                        Collectors.toList()));
    }

    @Override
    public Map<Pair<String, Pair<String,String>>, List<User>>  findUsersCountryAndStateAndCityWise() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .collect(Collectors.groupingBy(user ->
                        new Pair<>(
                                user.getCountry(),
                                new Pair<>(user.getState(), user.getCity())),
                        Collectors.toList()));
    }

    @Override
    public Map<String, List<User>> sortUsersByBirthdate() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return (userList.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(User::getBirthdate, Comparator.nullsLast(Comparator.naturalOrder())))
                        .thenComparing(Comparator.nullsLast(Comparator.comparing(User::getFirst_name, Comparator.nullsLast(Comparator.naturalOrder())))))
                .collect(Collectors.groupingBy(User::getFirst_name)));
    }

    @Override
    public Map<String, List<User>> collectByEmail() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .distinct()
                .sorted(Comparator.comparing(User::getEmail, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(User::getEmail));
    }

    @Override
    public Map<String, List<User>> adultUsers() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .filter(user -> user.getBirthdate()!=null)
                .sorted(Comparator.comparing(user -> Period.between(user.getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears() ))
                .filter(user -> Period.between(user.getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears() >= 18)
                .collect(Collectors.groupingBy(User::getFirst_name));
    }

    @Override
    public Set<String> uniqueCityNames() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .filter(Objects::nonNull)
                .map(User::getCity)
                .collect(Collectors.toSet());
    }


}
