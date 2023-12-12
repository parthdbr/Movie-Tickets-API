package com.movie.ticket.DTO;


import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO<T> {
    public String subject;
    T someDTO;
}
