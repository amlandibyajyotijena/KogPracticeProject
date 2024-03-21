package com.example.demo.repo;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.helper.RefreshableCRUDRepository;
import com.example.demo.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends RefreshableCRUDRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);
}