package com.movie.ticket.service;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class OtpService {


    public int genereateOtp(String key) {
        Random random = new Random();
        return Integer.parseInt(new DecimalFormat("000000").format(new Random().nextInt(999999)));
    }


}
