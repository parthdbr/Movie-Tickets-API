package com.movie.ticket.decorator;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class categoryBookedSeats {
    String category;
    List<Integer> booked_seats;
}
