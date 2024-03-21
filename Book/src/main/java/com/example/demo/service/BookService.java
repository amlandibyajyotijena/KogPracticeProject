package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Book;
import com.example.demo.repo.BookRepo;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

	@Autowired
	private BookRepo bookRepository;

	// Service methods to interact with the BookRepository
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book getBookById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	public Book createBook(Book book) {
		return bookRepository.save(book);
	}

	public Book updateBook(Long id, Book updatedBook) {
	    Optional<Book> optionalBook = bookRepository.findById(id);
	    if (optionalBook.isPresent()) {
	        Book book = optionalBook.get();
	        book.setName(updatedBook.getName());
	        book.setAuthor(updatedBook.getAuthor());
	        // Update other properties as needed
	        
	        // Save the updated book to the repository
	        return bookRepository.save(book);
	    }
	    return null;
	}

	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}
}
