package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	private int userId;
	private String userName;
	private String userEmail;
	private List<Book> books=new ArrayList<>();
     
	public void addBook(Book book) {
		books.add(book);
	}
}
