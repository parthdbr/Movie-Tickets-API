package com.movie.ticket.repository;

import java.util.List;

public interface UserCriteriaRepository {
    boolean seatsFound(List<Integer> bookSeats, String categoryName);
}
