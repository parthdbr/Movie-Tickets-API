package com.movie.ticket.repository;

import com.movie.ticket.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends MongoRepository<User, String> {

    User findByEmailContainingAndSoftDeleteIsFalse(String email);

     List<User> findBySoftDeleteIsFalse();

    List<User> findByCategoryAndSoftDeleteIsFalse(String category);
}
