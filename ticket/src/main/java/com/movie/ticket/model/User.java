package com.movie.ticket.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    String id;
    String first_name;
    String last_name;
    String email;
    String password;
    String category;
    List<Integer> booked_seats;
    List<Role> roles;
    boolean softDelete;
    boolean isAllowed;


    @Override
    public String toString(){
        return "FirstName : "+first_name+", LastName "+last_name+", Email : "+email+", Password : "+password+", Roles : "+roles;
    }
}
