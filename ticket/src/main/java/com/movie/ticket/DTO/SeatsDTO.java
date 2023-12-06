package com.movie.ticket.DTO;

import lombok.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatsDTO {
    String email;

    String category;

    List<Integer> booked_seats;
}
