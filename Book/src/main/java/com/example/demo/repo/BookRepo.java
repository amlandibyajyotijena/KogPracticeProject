package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

}
