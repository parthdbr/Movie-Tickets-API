package com.movie.ticket.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class UserNotExistsException extends Throwable{
    public UserNotExistsException(String e) {
        super(e);
        log.info("User Exists");
    }

    public UserNotExistsException() {

        super();
        log.info("User Exists");
    }
}
