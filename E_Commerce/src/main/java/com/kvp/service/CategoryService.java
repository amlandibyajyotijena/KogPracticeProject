package com.kvp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kvp.dto.CategoryDTO;
import com.kvp.dto.CategoryResponse;
import com.kvp.entities.Category;
import com.kvp.entities.Product;
import com.kvp.repo.CategoryRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class  CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	public CategoryDTO createCategory(Category category) throws Exception {
		Category savedCategory = categoryRepo.findByCategoryName(category.getCategoryName());

		if (savedCategory != null) {
			throw new Exception("Category with the name '" + category.getCategoryName() + "' already exists !!!");
		}

		savedCategory = categoryRepo.save(category);

		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws Exception {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Category> pageCategories = categoryRepo.findAll(pageDetails);

		List<Category> categories = pageCategories.getContent();

		if (categories.size() == 0) {
			throw new Exception("No category is created till now");
		}

		List<CategoryDTO> categoryDTOs = categories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

		CategoryResponse categoryResponse = new CategoryResponse();

		categoryResponse.setContent(categoryDTOs);
		categoryResponse.setPageNumber(pageCategories.getNumber());
		categoryResponse.setPageSize(pageCategories.getSize());
		categoryResponse.setTotalElements(pageCategories.getTotalElements());
		categoryResponse.setTotalPages(pageCategories.getTotalPages());
		categoryResponse.setLastPage(pageCategories.isLast());

		return categoryResponse;
	}

	public CategoryDTO updateCategory(Category category, Long categoryId) throws Exception {
		Category savedCategory = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new Exception("Category"+categoryId));

		category.setCategoryId(categoryId);

		savedCategory = categoryRepo.save(category);

		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	
	public String deleteCategory(Long categoryId) throws Exception {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new Exception("Category"+categoryId));

		List<Product> products = category.getProducts();

		products.forEach(product -> {
			try {
				productService.deleteProduct(product.getProductId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		categoryRepo.delete(category);

		return "Category with categoryId: " + categoryId + " deleted successfully !!!";
	}

}