package com.movie.ticket.repository;

import java.lang.reflect.InvocationTargetException;

public interface EmailDescCriteriaRepository {
    String findTemplateBySubject(String subject, String key) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
}
