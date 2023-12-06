package com.movie.ticket.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CategoryExistsException extends Throwable {
    public CategoryExistsException(String e) {
        super(e);
        log.info("Category Exists");
    }

    public CategoryExistsException() {

        super();
        log.info("Category Exists");
    }
}
