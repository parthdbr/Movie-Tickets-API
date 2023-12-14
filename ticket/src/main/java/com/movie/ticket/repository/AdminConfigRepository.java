package com.movie.ticket.repository;

import com.movie.ticket.model.AdminConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminConfigRepository extends MongoRepository<AdminConfig, String> {
    List<AdminConfig> findAll();
}
