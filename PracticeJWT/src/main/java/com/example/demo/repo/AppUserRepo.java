package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.AppUser;

public interface AppUserRepo extends MongoRepository<AppUser, String>{

	boolean existsByUsername(String username);

	Optional<AppUser> findByUsername(String username);

}
