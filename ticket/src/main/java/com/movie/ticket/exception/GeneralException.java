package com.movie.ticket.exception;

public class GeneralException extends RuntimeException{
    public GeneralException(String e) {
        super(e);
    }

    public GeneralException() {
        super();
    }
}
