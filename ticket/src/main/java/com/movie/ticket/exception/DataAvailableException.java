package com.movie.ticket.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataAvailableException extends RuntimeException{
    public DataAvailableException(String e) {
        super(e);
    }

}
