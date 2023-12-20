package com.movie.ticket.DTO;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    String first_name;
    String last_name;
    String email;
    String password;
    String country;
    String state;
    String city;
    Date birthdate;
}
