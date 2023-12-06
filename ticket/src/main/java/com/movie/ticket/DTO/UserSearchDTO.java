package com.movie.ticket.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSearchDTO {

    String search;
    String field;
    String order;
    String category;
    int page;
    int size;

}
