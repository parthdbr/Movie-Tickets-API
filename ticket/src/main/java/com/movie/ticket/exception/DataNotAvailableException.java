package com.movie.ticket.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataNotAvailableException extends RuntimeException{
    public DataNotAvailableException(String e) {
        super(e);
    }

}
