package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Book;
import com.example.demo.repo.BookRepo;

@Service
public class BookService {
	@Autowired
	private BookRepo repository;

	public Book saveBook(Book product) {
		return repository.save(product);
	}

	public List<Book> getBooks() {
		return repository.getAllBooks();
	}

	public Book getBookById(int id) {
		return repository.findById(id);
	}

	public String deleteBook(int id) {
		repository.delete(id);
		return "product removed !! " + id;
	}

	public Book updateBook(Book product) {
		return repository.update(product);
	}
}
