package com.melon.controller;

import java.net.URI;
import java.util.*;
import com.melon.entity.Category;
import com.melon.entity.Product;
import com.melon.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Api(tags = "Products API")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/categories")
    @ApiOperation("all products")
    public ResponseEntity<List<Category>> getCategories() {

        return ResponseEntity.ok().body(productService.getCategories());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){

        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id){

        Product product = productService.getProduct(id);
        return product != null ? ResponseEntity.ok(product): ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<Product> addProduct(@RequestBody Product product){

        System.out.println(product);
        if (product != null){
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/products").toUriString());
            return ResponseEntity.created(uri).body(productService.addProduct(product));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping()
    public ResponseEntity<Product> updateProduct(@RequestBody Product product){

        Product updatedProduct = productService.updateProduct(product);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct): ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id){

        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
