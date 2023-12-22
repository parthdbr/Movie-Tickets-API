package com.movie.ticket.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgeDTO {
    String id;
    String full_name;
    String birthdate;
    String age;
}
