package com.movie.ticket.decorator;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse<T> {
    T data;
    Response status;
}
