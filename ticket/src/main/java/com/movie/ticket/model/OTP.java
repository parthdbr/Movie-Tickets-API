package com.movie.ticket.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTP {
    String username;
    int otp;
}
