package com.movie.ticket.DTO;


import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    String subject;
    String first_name;
    String last_name;
    String email;
    String password;
    boolean SoftDelete;
    boolean isAllowed;
}
