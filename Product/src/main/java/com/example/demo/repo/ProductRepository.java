package com.example.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Product;


@Repository
public interface ProductRepository extends MongoRepository<Product, Integer>{
	@Query("{'name':?0}")
    Product findByName(String name);
}
