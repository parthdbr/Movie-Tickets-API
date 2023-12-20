package com.movie.ticket.repository;

import com.movie.ticket.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StreamRepositoryImpl implements StreamRepository{

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Map<?, ?> findUsersCityWise() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .collect(Collectors.groupingBy(User::getCity));

    }

    @Override
    public Map<?, ?> findUsersStateAndCityWise() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .collect(Collectors.groupingBy(User::getState, Collectors.groupingBy(User::getCity)));
    }

    @Override
    public Map<?, ?> findUsersCountryAndStateAndCityWise() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .collect(Collectors.groupingBy(User::getCountry, Collectors.groupingBy(User::getState, Collectors.groupingBy(User::getCity))));
    }
}
