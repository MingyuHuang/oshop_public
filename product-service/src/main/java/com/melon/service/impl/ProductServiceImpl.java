package com.melon.service.impl;

import com.melon.entity.Category;
import com.melon.entity.Product;
import com.melon.repository.CategoryRepository;
import com.melon.repository.ProductRepository;
import com.melon.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    private final String exchangeName = "product_exchange";

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {

        if (category != null){

            return categoryRepository.save(category);
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Integer id) {

        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product addProduct(Product product) {

        if (product != null){
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public Product updateProduct(Product product) {

        Product existedProduct = productRepository.findById(product.getId()).get();
        if (existedProduct != null){
            existedProduct.setTitle(product.getTitle());
            existedProduct.setCategory(product.getCategory());
            existedProduct.setPrice(product.getPrice());
            existedProduct.setImageUrl(product.getImageUrl());
        }
        Product updatedProduct = productRepository.save(existedProduct);
        rabbitTemplate.convertAndSend(exchangeName, "productRoutingKey", updatedProduct);
        return updatedProduct;
    }

    @RabbitListener(queues = "queue_navbar")
    public void receiveTest(Product product){

        System.out.println("Received: " + product);
    }

    @Override
    public void deleteProduct(Integer id) {
        try{

            productRepository.deleteById(id);
        }catch (EmptyResultDataAccessException exception){
            System.out.println("Product is not existed");
        }
    }
}
