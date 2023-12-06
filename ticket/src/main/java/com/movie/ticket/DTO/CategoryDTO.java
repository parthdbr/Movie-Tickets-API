package com.movie.ticket.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    String name;
    String price;
    int start_seat_number;
    int end_seat_number;

}
