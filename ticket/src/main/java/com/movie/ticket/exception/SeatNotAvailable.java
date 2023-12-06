package com.movie.ticket.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SeatNotAvailable extends Throwable{
    public SeatNotAvailable(String e) {
        super(e);
        log.info("Entered Seat/Seats not available");
    }

    public SeatNotAvailable() {

        super();
        log.info("Entered Seat/Seats not available");
    }
}
