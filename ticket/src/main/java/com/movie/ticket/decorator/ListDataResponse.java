package com.movie.ticket.decorator;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListDataResponse<T> {
    List<T> data;
    Response status;
}
