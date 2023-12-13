package com.movie.ticket.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
    private static final Integer EXPIRE_MINS = 10;
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
    }

    public int genereateOtp(String key) {
        Random random = new Random();
        int otp = Integer.parseInt(new DecimalFormat("000000").format(new Random().nextInt(999999)));
        otpCache.put(key, otp);
        return otp;
    }

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


}
