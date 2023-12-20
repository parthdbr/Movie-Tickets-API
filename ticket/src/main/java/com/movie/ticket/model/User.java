package com.movie.ticket.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends CreationUpdation {
    @Id
    String id;
    String first_name;
    String last_name;
    String email;
    String password;
    String country;
    String state;
    String city;
    Date birthdate;
    int otp;
    Date generateOtpTime;
    int otpCount;
    String category;
    List<Integer> booked_seats;
    List<Role> roles;
    boolean softDelete;
    boolean active;
//    CreationUpdation creationUpdation;


    @Override
    public String toString(){
        return "ID : "+id+", FirstName : "+first_name+", LastName "+last_name+", Email : "+email+", Password : "+password+", Roles : "+roles;
    }
}
