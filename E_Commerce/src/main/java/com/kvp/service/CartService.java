package com.kvp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kvp.dto.CartDTO;
import com.kvp.dto.ProductDTO;
import com.kvp.entities.Cart;
import com.kvp.entities.CartItem;
import com.kvp.entities.Product;
import com.kvp.repo.CartItemRepo;
import com.kvp.repo.CartRepo;
import com.kvp.repo.ProductRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class  CartService {

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CartItemRepo cartItemRepo;

	@Autowired
	private ModelMapper modelMapper;

	public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) throws Exception {

		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new Exception("Cart"+cartId));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new Exception("Product"+ productId));

		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem != null) {
			throw new Exception("Product " + product.getProductName() + " already exists in the cart");
		}

		if (product.getQuantity() == 0) {
			throw new Exception(product.getProductName() + " is not available");
		}

		if (product.getQuantity() < quantity) {
			throw new Exception("Please, make an order of the " + product.getProductName()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}

		CartItem newCartItem = new CartItem();

		newCartItem.setProduct(product);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(quantity);
		newCartItem.setDiscount(product.getDiscount());
		newCartItem.setProductPrice(product.getSpecialPrice());

		cartItemRepo.save(newCartItem);

		product.setQuantity(product.getQuantity() - quantity);

		cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

		List<ProductDTO> productDTOs = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		cartDTO.setProducts(productDTOs);

		return cartDTO;

	}

	public List<CartDTO> getAllCarts() throws Exception {
		List<Cart> carts = cartRepo.findAll();

		if (carts.size() == 0) {
			throw new Exception("No cart exists");
		}

		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

			List<ProductDTO> products = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			cartDTO.setProducts(products);

			return cartDTO;

		}).collect(Collectors.toList());

		return cartDTOs;
	}

	public CartDTO getCart(String emailId, Long cartId) throws Exception {
		Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

		if (cart == null) {
			throw new Exception("Cart"+cartId);
		}

		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		
		List<ProductDTO> products = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		cartDTO.setProducts(products);

		return cartDTO;
	}

	public void updateProductInCarts(Long cartId, Long productId) throws Exception {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new Exception("Cart"+ cartId));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new Exception("Product"+ productId));

		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new Exception("Product " + product.getProductName() + " not available in the cart!!!");
		}

		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

		cartItem.setProductPrice(product.getSpecialPrice());

		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

		cartItem = cartItemRepo.save(cartItem);
	}

	public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) throws Exception {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new Exception("Cart"+cartId));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new Exception("Product"+productId));

		if (product.getQuantity() == 0) {
			throw new Exception(product.getProductName() + " is not available");
		}

		if (product.getQuantity() < quantity) {
			throw new Exception("Please, make an order of the " + product.getProductName()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}

		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new Exception("Product " + product.getProductName() + " not available in the cart!!!");
		}

		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

		product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);

		cartItem.setProductPrice(product.getSpecialPrice());
		cartItem.setQuantity(quantity);
		cartItem.setDiscount(product.getDiscount());

		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * quantity));

		cartItem = cartItemRepo.save(cartItem);

		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

		List<ProductDTO> productDTOs = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		cartDTO.setProducts(productDTOs);

		return cartDTO;

	}

	public String deleteProductFromCart(Long cartId, Long productId) throws Exception {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new Exception("Cart"+cartId));

		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new Exception("Product"+ productId);
		}

		cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

		Product product = cartItem.getProduct();
		product.setQuantity(product.getQuantity() + cartItem.getQuantity());

		cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);

		return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
	}

}