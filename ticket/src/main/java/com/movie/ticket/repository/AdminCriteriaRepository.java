package com.movie.ticket.repository;

public interface AdminCriteriaRepository {
    String checkSeatsAvailable(int startSeatNumber, int endSeatNumber);
}
