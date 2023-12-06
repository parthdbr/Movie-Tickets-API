package com.movie.ticket.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SeatsNotEmptyException extends Throwable{
    public SeatsNotEmptyException(String e) {
        super(e);
        log.info("Seats already booked!");
    }

    public SeatsNotEmptyException() {

        super();
        log.info("Seats already booked");
    }
}
