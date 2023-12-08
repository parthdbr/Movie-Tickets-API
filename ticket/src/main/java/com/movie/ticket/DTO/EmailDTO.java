package com.movie.ticket.DTO;


import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    String from;
    String cc;
    String bcc;
    String replyTo;
    String[] to;
    String subject;
    String text;
}
