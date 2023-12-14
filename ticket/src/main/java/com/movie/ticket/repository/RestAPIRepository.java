package com.movie.ticket.repository;

import com.movie.ticket.model.RestAPIs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestAPIRepository extends MongoRepository<RestAPIs, String> {

    RestAPIs findByName(String api);
    boolean existsByName(String name);
}
