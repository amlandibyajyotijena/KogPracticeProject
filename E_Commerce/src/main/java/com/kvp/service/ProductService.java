package com.kvp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kvp.dto.CartDTO;
import com.kvp.dto.ProductDTO;
import com.kvp.dto.ProductResponse;
import com.kvp.entities.Cart;
import com.kvp.entities.Category;
import com.kvp.entities.Product;
import com.kvp.repo.CartRepo;
import com.kvp.repo.CategoryRepo;
import com.kvp.repo.ProductRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ProductService {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private CartService cartService;

	@Autowired
	private FileService fileservice;

	@Autowired
	private ModelMapper modelMapper;

	private String path;

	public ProductDTO addProduct(Long categoryId, Product product) throws Exception {

		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new Exception("Category"+categoryId));

		boolean isProductNotPresent = true;

		List<Product> products = category.getProducts();

		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getProductName().equals(product.getProductName())
					&& products.get(i).getDescription().equals(product.getDescription())) {

				isProductNotPresent = false;
				break;
			}
		}

		if (isProductNotPresent) {
			product.setImage("default.png");

			product.setCategory(category);

			double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
			product.setSpecialPrice(specialPrice);

			Product savedProduct = productRepo.save(product);

			return modelMapper.map(savedProduct, ProductDTO.class);
		} else {
			throw new Exception("Product already exists !!!");
		}
	}

	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Product> pageProducts = productRepo.findAll(pageDetails);

		List<Product> products = pageProducts.getContent();

		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();

		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());

		return productResponse;
	}

	public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder) throws Exception {

		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new Exception("Category"+categoryId));

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Product> pageProducts = productRepo.findAll(pageDetails);

		List<Product> products = pageProducts.getContent();

		if (products.size() == 0) {
			throw new Exception(category.getCategoryName() + " category doesn't contain any products !!!");
		}

		List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class))
				.collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();

		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());

		return productResponse;
	}

	public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws Exception {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Product> pageProducts = productRepo.findByProductNameLike(keyword, pageDetails);

		List<Product> products = pageProducts.getContent();
		
		if (products.size() == 0) {
			throw new Exception("Products not found with keyword: " + keyword);
		}

		List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class))
				.collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();

		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());

		return productResponse;
	}

	public ProductDTO updateProduct(Long productId, Product product) throws Exception {
		Product productFromDB = productRepo.findById(productId)
				.orElseThrow(() -> new Exception("Product"+productId));

		if (productFromDB == null) {
			throw new Exception("Product not found with productId: " + productId);
		}

		product.setImage(productFromDB.getImage());
		product.setProductId(productId);
		product.setCategory(productFromDB.getCategory());

		double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
		product.setSpecialPrice(specialPrice);

		Product savedProduct = productRepo.save(product);

		List<Cart> carts = cartRepo.findCartsByProductId(productId);

		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

			List<ProductDTO> products = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			cartDTO.setProducts(products);

			return cartDTO;

		}).collect(Collectors.toList());

		cartDTOs.forEach(cart -> {
			try {
				cartService.updateProductInCarts(cart.getCartId(), productId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	public ProductDTO updateProductImage(Long productId, MultipartFile image) throws Exception {
		Product productFromDB = productRepo.findById(productId)
				.orElseThrow(() -> new Exception("Product"+productId));

		if (productFromDB == null) {
			throw new Exception("Product not found with productId: " + productId);
		}
		
		String fileName = fileservice.uploadImage(path, image);
		
		productFromDB.setImage(fileName);
		
		Product updatedProduct = productRepo.save(productFromDB);
		
		return modelMapper.map(updatedProduct, ProductDTO.class);
	}
	
	public String deleteProduct(Long productId) throws Exception {

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new Exception("Product"+productId));

		List<Cart> carts = cartRepo.findCartsByProductId(productId);

		carts.forEach(cart -> {
			try {
				cartService.deleteProductFromCart(cart.getCartId(), productId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		productRepo.delete(product);

		return "Product with productId: " + productId + " deleted successfully !!!";
	}

}