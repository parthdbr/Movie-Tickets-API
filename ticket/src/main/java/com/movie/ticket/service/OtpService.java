package com.movie.ticket.service;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class OtpService {
/*    private static final Integer EXPIRE_MINS = 10;
    private LoadingCache<String, Integer> otpCache;

    public OtpService(){
        super();
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });
    }*/

    public int genereateOtp(String key) {
        Random random = new Random();
        //        otpCache.put(key, otp);
        return Integer.parseInt(new DecimalFormat("000000").format(new Random().nextInt(999999)));
    }
/*

    public int getOtp(String key) {
        try{
            return otpCache.get(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clearOtp(String key) {
        otpCache.invalidate(key);
    }

*/

}
