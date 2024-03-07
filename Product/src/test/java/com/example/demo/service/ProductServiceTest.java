package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.dto.Product;
import com.example.demo.repo.ProductRepository;

class ProductServiceTest {
	@InjectMocks
	private ProductService pService;

	@Mock
	private ProductRepository repository;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSaveProduct() {
		Product product = new Product(1, "A", "ddw", 10, 20);

		when(repository.save(product)).thenReturn(product);

		Product savedProduct = pService.saveProduct(product);

		verify(repository, times(1)).save(product);
		assertEquals(product, savedProduct);
	}

	@Test
	void testGetProducts() {
		List<Product> expectedProducts = Arrays.asList(new Product(1, "A", "ddw", 10, 20));
		when(repository.findAll()).thenReturn(expectedProducts);

		List<Product> getProducts = pService.getProducts();
		assertEquals(expectedProducts, getProducts);
	}

	@Test
	void testGetProductById() {
		Optional<Product> p = Optional.of(new Product(1, "BDHF", "ddeww", 10, 20));

		when(repository.findById(1)).thenReturn(p);

		Optional<Product> actualProduct = Optional.of(pService.getProductById(1));

		assertEquals(p, actualProduct);
	}

	@Test
	void testDeleteProduct() {
		Product product = new Product(1, "A", "ddw", 10, 20);
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        String result = pService.deleteProduct(product.getId());

        verify(repository, times(1)).deleteById(product.getId());

        assertEquals("Product removed: " + product.getId(), result);
	}

	@Test
	void testUpdateProduct() {
		  Product product = new Product(1, "A", "ddw", 10, 20);
	        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
	        when(repository.save(product)).thenReturn(product);

	        String result = pService.updateProduct(product.getId(), product);

	        verify(repository, times(1)).findById(product.getId());
	        verify(repository, times(1)).save(product);

	        assertEquals("Product updated successfully", result);
	}

	@Test
	void testGetProductByName() {
		Product p = new Product(1, "BDHF", "ddeww", 10, 20);

		when(repository.findByName("BDHF")).thenReturn(p);

		Product actualProduct = pService.getProductByName("BDHF");

		assertEquals(p, actualProduct);
	}

}
