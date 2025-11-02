package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
     private final ProductRepository productRepository;
     private final ProductMapper productMapper;
     @GetMapping
     public List<ProductDto> getAllProducts(@RequestParam(name = "categoryId" , required = false) Byte CategoryId){
         List<Product> products;
         if(CategoryId!=null){
              products = productRepository.findByCategoryId(CategoryId);
         }
         else{
             products = productRepository.findAll();
         }
         return products.stream().map(productMapper::toDto).toList();
     }
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductDto> getproduct(@PathVariable Long id) {
//        var product = productRepository.findById(id).orElse(null);
//        if(product == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(productMapper.toDto(product));
//    }
}
