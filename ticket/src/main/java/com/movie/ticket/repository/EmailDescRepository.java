package com.movie.ticket.repository;

import com.movie.ticket.model.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailDescRepository extends MongoRepository<Email, String> {

}
