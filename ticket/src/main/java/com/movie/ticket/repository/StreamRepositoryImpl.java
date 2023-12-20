package com.movie.ticket.repository;

import com.movie.ticket.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Override
    public Map<?, ?> sortUsersByBirthdate() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(User::getBirthdate))
                        .thenComparing(Comparator.nullsLast(Comparator.comparing(User::getFirst_name))))
                .collect(Collectors.groupingBy(User::getFirst_name));
    }

    @Override
    public Map<?, ?> collectByEmail() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .distinct()
                .sorted(Comparator.comparing(User::getEmail, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(User::getEmail));
    }

    @Override
    public Map<?, ?> adultUsers() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .sorted(Comparator.comparing(user -> Period.between(user.getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears() ))
                .filter(user -> Period.between(user.getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears() >= 18)
                .collect(Collectors.groupingBy(User::getFirst_name));
    }

    @Override
    public Map<?, ?> uniqueCityNames() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList.stream()
                .map(User::getCity)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(city -> city, city -> true));
    }


}
