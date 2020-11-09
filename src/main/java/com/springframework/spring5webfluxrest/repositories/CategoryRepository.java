package com.springframework.spring5webfluxrest.repositories;

import com.springframework.spring5webfluxrest.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category,String> {
}
