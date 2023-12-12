package com.movie.ticket.repository;

public interface EmailDescCriteriaRepository {
    String findTemplateBySubject(String subject);
}
