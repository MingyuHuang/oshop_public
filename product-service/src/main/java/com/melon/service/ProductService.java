package com.melon.service;

import com.melon.entity.Category;
import com.melon.entity.Product;

import java.util.List;

public interface ProductService {

    List<Category> getCategories();

    Category addCategory(Category category);

    List<Product> getAllProducts();

    Product getProduct(Integer id);

    Product addProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(Integer id);
}
