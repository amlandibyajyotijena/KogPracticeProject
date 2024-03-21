package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Book;
import com.example.demo.entities.User;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/library")
public class LibraryController {
	@Autowired
	UserService uService;
	
	@Autowired
	BookService bService;

	@PostMapping("/user")
	public String addUser(@RequestBody User user) {
		uService.saveUser(user);
		return "user added successfully";
	}

	@PostMapping("/book")
	public String addBook(@RequestBody Book book) {
		bService.saveBook(book);
		return "book added successfully";
	}

	@GetMapping("/user")
	public List<User> getAllUsers() {
		return uService.getUsers();
	}

	@GetMapping("/book")
	public List<Book> getAllBooks() {
		return bService.getBooks();
	}

	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable int id) {
		User u=uService.getUserById(id);
		if(u!=null) {
			return u;
		}
		return null;
	}

	@GetMapping("/book/{id}")
	public Book getBookById(@PathVariable int id) {
		Book b=bService.getBookById(id);
		if(b!=null) {
			return b;
		}
		return null;
	}

	@PostMapping("/assign/{userId}/{bookId}")
	public String assignBookToUser(@PathVariable int userId, @PathVariable int bookId) {

		User u = getUserById(userId);
		Book b = getBookById(bookId);
		if (u != null && b != null) {
			u.addBook(b);
			return "Book assigned to User";
		}
		return "User or Book not Found";
	}

	@PutMapping("/user/{id}")
	public String updateUser(@PathVariable int id, @RequestBody User updatedUser) {
		User u = getUserById(id);
		if (u != null) {
			u.setUserName(updatedUser.getUserName());
			u.setUserEmail(updatedUser.getUserEmail());
			return "User updated Successfully";
		}
		return "User Not Found";
	}

	@PutMapping("/book/{id}")
	public String updateBook(@PathVariable int id, @RequestBody Book updatedBook) {
		Book b = getBookById(id);
		if (b != null) {
			b.setBookName(updatedBook.getBookName());
			b.setAuthor(updatedBook.getAuthor());
			return "Book updated Successfully";
		}
		return "Book Not Found";
	}

	@DeleteMapping("/user/{id}")
	public String deleteUser(@PathVariable int id) {
		
        	return uService.deleteUser(id);	
	}

	@DeleteMapping("/book/{id}")
	public String deleteBook(@PathVariable int id) {
		return bService.deleteBook(id);
	}

}
