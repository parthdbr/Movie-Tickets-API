package com.movie.ticket.repository;

import com.movie.ticket.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    List<Category> findBySoftDeleteIsFalse();

    Category findByNameContainingAndSoftDeleteIsFalse(String name);

    Category findByIdAndSoftDeleteIsFalse(String id);
}
