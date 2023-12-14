package com.movie.ticket.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "RestAPIs")
public class RestAPIs {
    String name;
    List<String> roles;
}
