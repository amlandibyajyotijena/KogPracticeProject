package com.example.demo.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.Book;
@Repository
public class BookRepo {
	 private List<Book> books=new ArrayList<Book>();
	  
	  public void createBooks() {
		  books=List.of(
				  new Book(1, "HJJHH", "a@gmail.com"),
				  new Book(2, "HJHFFF", "amu@gmail.com")
				  );
	  }
	  
	  public List<Book> getAllBooks(){
		  return books;
	  }
	  
	  public Book findById(int id) {
		  for(int i=0;i<books.size();i++) {
			  if(books.get(i).getBookId()==(id)) {
				  return books.get(i);
			  }
		  }
		  return null;
	  }
	  

	  public List<Book> search(String name) {
	      return books.stream().filter(x -> x.getBookName().startsWith(name)).collect(Collectors.toList());
	  }

	  public Book save(Book u) {
	      Book user = new Book();
	      user.setBookId(u.getBookId());
	      user.setBookName(u.getBookName());
	      user.setAuthor(u.getAuthor());
	      books.add(user);
	      return user;
	  }

	  public String delete(Integer id) {
	      books.removeIf(x -> x.getBookId() == (id));
	      return null;
	  }

	  public Book update(Book user) {
	      int idx = 0;
	      int id = 0;
	      for (int i = 0; i < books.size(); i++) {
	          if (books.get(i).getBookId() == (user.getBookId())) {
	              id = user.getBookId();
	              idx = i;
	              break;
	          }
	      }

	      Book user1 = new Book();
	      user1.setBookId(id);
	      user1.setBookName(user.getBookName());
	      user1.setAuthor(user.getAuthor());
	      books.set(idx, user);
	      return user1;
	  }
}
