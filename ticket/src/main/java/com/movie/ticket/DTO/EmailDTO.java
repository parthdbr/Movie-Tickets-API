package com.movie.ticket.DTO;


import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    String fullName;
    String from;
    String cc;
    String bcc;
    String replyTo;
    String email;
    String subject;
    String text;
}
