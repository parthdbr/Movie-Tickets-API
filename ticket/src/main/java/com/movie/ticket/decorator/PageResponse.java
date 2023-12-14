package com.movie.ticket.decorator;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    List<T> users;

    long total_count;

    long total_page;

    long page_number;

    long size_of_page;

    Response status;
}
