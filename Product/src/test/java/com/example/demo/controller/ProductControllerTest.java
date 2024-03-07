package com.example.demo.controller;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.dto.Product;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testAddProduct() throws Exception {
		Product product = new Product(1, "A", "ddw", 10, 20);

		String productJson = objectMapper.writeValueAsString(product);

		when(productService.saveProduct(any(Product.class))).thenReturn(product);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/products").contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("A"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("ddw"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(10));

		verify(productService, times(1)).saveProduct(any(Product.class));
	}

	@Test
	void testFindAllProducts() throws Exception {

		Product product1 = new Product(1, "A", "ddw", 10, 20);
		Product product2 = new Product(2, "B", "xyz", 15, 25);
		List<Product> products = Arrays.asList(product1, product2);

		when(productService.getProducts()).thenReturn(products);

		mockMvc.perform(MockMvcRequestBuilders.get("/products")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("A"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("ddw"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(10))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("B"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("xyz"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(25))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(15));

		verify(productService, times(1)).getProducts();

	}

	@Test
	void testFindProductById() throws Exception {
		Product product1 = new Product(1, "A", "ddw", 10, 20);

		when(productService.getProductById(1)).thenReturn(product1);

		mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("A"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("ddw"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(10));

		verify(productService, times(1)).getProductById(1);
	}

	@Test
	void testFindProductByName() throws Exception {
		Product product1 = new Product(1, "A", "ddw", 10, 20);

		when(productService.getProductByName("A")).thenReturn(product1);

		mockMvc.perform(MockMvcRequestBuilders.get("/products/name/{name}", "A"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("A"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("ddw"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(10));

		verify(productService, times(1)).getProductByName("A");
	}

	@Test
	void testUpdateProduct() throws Exception {

		Product product = new Product(1, "A", "ddw", 10, 20);
		
		String updatedProductJson = objectMapper.writeValueAsString(product);
		when(productService.updateProduct(1, product)).thenReturn("Product updated successfully");

		mockMvc.perform(MockMvcRequestBuilders.put("/products/update/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
	               .content(updatedProductJson))
		
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Product updated successfully"));

		verify(productService, times(1)).updateProduct(1,product);

		
	}

	@Test
	void testDeleteProduct() throws Exception {
		Product product = new Product(1, "A", "ddw", 10, 20);
		
		when(productService.deleteProduct(product.getId())).thenReturn("Product removed");
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}",1).contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().string("Product removed"));

		
		verify(productService, times(1)).deleteProduct(1);
	}
}
