package com.movie.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "emailDesc")
public class Email {
    String subject;
    String first_name;
    String last_name;
    String email;
    String password;
    boolean SoftDelete;
    boolean isAllowed;

}
