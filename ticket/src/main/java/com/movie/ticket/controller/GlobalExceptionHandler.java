package com.movie.ticket.controller;

import com.movie.ticket.decorator.DataResponse;
import com.movie.ticket.decorator.Response;
import com.movie.ticket.exception.*;
import com.movie.ticket.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAvailableException.class)
    public DataResponse<User> dataAvailable(DataAvailableException exception) {

        DataResponse<User> response = new DataResponse<>();

        response.setStatus(new Response(HttpStatus.FOUND, exception.getMessage(), "302"));

        return response;

    }

    @ExceptionHandler(DataNotAvailableException.class)
    public DataResponse<User> dataNotAvailable(DataNotAvailableException exception) {

        DataResponse<User> response = new DataResponse<>();

        response.setStatus(new Response(HttpStatus.NOT_ACCEPTABLE, exception.getMessage(), "406"));

        return response;

    }



    @ExceptionHandler(GeneralException.class)
    public DataResponse<User> generalException(GeneralException exception) {

        DataResponse<User> response = new DataResponse<>();

        response.setStatus(new Response(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), "500"));

        return response;

    }


    @ExceptionHandler(ValidationException.class)
    public DataResponse<User> validationException(ValidationException validationException) {
        DataResponse<User> response = new DataResponse<>();

        response.setStatus(new Response(HttpStatus.NOT_ACCEPTABLE, validationException.getMessage(), "406"));

        return response;
    }
}
