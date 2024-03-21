package com.kvp.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kvp.entity.ImageData;

public interface ImageRepo extends JpaRepository<ImageData, Long> {
	Optional<ImageData> findByName(String name);

}
