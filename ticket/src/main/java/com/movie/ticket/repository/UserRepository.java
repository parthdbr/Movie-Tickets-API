package com.movie.ticket.repository;

import com.movie.ticket.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends MongoRepository<User, String> {

    User findByEmailContainingAndSoftDeleteIsFalse(String email);

     Page<User> findBySoftDeleteIsFalse(Pageable pageable);

    List<User> findByCategoryIgnoreCaseAndSoftDeleteIsFalse(String category);

    User findByIdAndSoftDeleteIsFalse(String id);

    List<User> findByActiveIsFalseAndSoftDeleteIsFalse();
}
