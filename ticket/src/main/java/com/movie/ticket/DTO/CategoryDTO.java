package com.movie.ticket.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    String name;
    String price;
    String start_seat_number;
    String end_seat_number;

}
