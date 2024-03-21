package com.example.demo.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.dto.Book;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
class BookControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BookService bookService;
	
	@Autowired
	ObjectMapper objectMapper;

	@Test
	void testGetAllBooks() throws Exception {
		Book book1=new Book(1L, "a", "b");
		Book book2=new Book(2L, "a", "b");
		
		List<Book> books=Arrays.asList(book1,book2);
		
		when(bookService.getAllBooks()).thenReturn(books);
		
		mockMvc.perform(get("/books")).andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(books.size())).andDo(print());
	}

	@Test
	void testGetBookById() throws Exception {
		Book book1=new Book(1L, "a", "b");
		when(bookService.getBookById(1L)).thenReturn(book1);
		
		mockMvc.perform(get("/books/{id}",1L)).andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L)).andDo(print());

	}

	@Test
	void testCreateBook() throws Exception {
		Book book1=new Book(1L, "a", "b");
		when(bookService.createBook(book1)).thenReturn(book1);
		
		mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(book1))).andExpect(status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L)).andDo(print());

	}

	@Test
	void testUpdateBook() throws JsonProcessingException, Exception {
		Book book1=new Book(1L, "a", "b");
		Book book2=new Book(1L, "c", "d");
		when(bookService.updateBook(1L, book2)).thenReturn(book2);
		
		mockMvc.perform(put("/books/{id}",1L).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(book2))).andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("c"))
		.andDo(print());

	}

  	@Test
	void testDeleteBook() throws JsonProcessingException, Exception {
  		Book book1=new Book(1L, "a", "b");
		doNothing().when(bookService).deleteBook(1L);
		
		mockMvc.perform(delete("/books/{id}",1L)).andExpect(status().isNoContent())
		.andDo(print());

	} 

}
