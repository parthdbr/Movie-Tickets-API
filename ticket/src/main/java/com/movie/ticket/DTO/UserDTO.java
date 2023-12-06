package com.movie.ticket.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    String first_name;
    String last_name;
    String email;
    String password;
}
