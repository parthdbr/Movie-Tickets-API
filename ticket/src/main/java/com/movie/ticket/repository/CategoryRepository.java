package com.movie.ticket.repository;

import com.movie.ticket.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    Page<Category> findBySoftDeleteIsFalse(Pageable pageable);

    Category findByNameContainingAndSoftDeleteIsFalse(String name);

    Category findByIdAndSoftDeleteIsFalse(String id);
}
