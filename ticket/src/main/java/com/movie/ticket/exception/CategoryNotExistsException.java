package com.movie.ticket.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CategoryNotExistsException extends  Throwable{
    public CategoryNotExistsException(String e) {
        super(e);
        log.info("Category Exists");
    }

    public CategoryNotExistsException() {

        super();
        log.info("Category Exists");
    }
}
