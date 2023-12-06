package com.movie.ticket.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Document(collection = "category")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    String id;

    String name;

    String price;

    int start_seat_number;

    int end_seat_number;


    boolean softDelete;

}





