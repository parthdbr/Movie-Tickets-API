package com.movie.ticket.Annotation;

import com.movie.ticket.model.Role;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Access {
    Role[] roles() default Role.USER;

}
