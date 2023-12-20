package com.movie.ticket.repository;

import java.util.Map;

public interface StreamRepository {
    Map<?,?> findUsersCityWise();

    Map<?,?> findUsersStateAndCityWise();

    Map<?,?> findUsersCountryAndStateAndCityWise();

    Map<?,?> sortUsersByBirthdate();

    Map<?,?> collectByEmail();
}
