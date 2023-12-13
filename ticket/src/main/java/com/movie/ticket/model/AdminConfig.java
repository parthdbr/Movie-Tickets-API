package com.movie.ticket.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "adminConfig")
public class AdminConfig {

    EmailConfiguration emailConfiguration;
}
