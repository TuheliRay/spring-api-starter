package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
     private final ProductRepository productRepository;
     private final ProductMapper productMapper;
     private final CategoryRepository categoryRepository;
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
     @PostMapping
     public ResponseEntity<ProductDto> createProduct(
             @RequestBody ProductDto productDto ,
             UriComponentsBuilder uriBuilder
             ){
         var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
         if(category==null){
             return ResponseEntity.badRequest().build();
         }
         var product = productMapper.toEntity(productDto);
         product.setCategory(category);
         productRepository.save(product);
         productDto.setId(product.getId());
         var newProductDto = productMapper.toDto(product);
         var uri = uriBuilder.path("/products/{id}").buildAndExpand(newProductDto.getId()).toUri();
         return  ResponseEntity.created(uri).body(newProductDto);
     }
     @PutMapping("/{id}")
     public ResponseEntity<ProductDto> updateProduct(
             @PathVariable Long id,
             @RequestBody ProductDto productDto){
         var product =  productRepository.findById(id).orElse(null);
         var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
         if(category==null){
             return ResponseEntity.badRequest().build();
         }
         if(product==null){
             return ResponseEntity.notFound().build();
         }
         productMapper.updateProduct(productDto , product);
         product.setCategory(category);
         productRepository.save(product);
         return ResponseEntity.ok(productMapper.toDto(product));
     }
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteProduct(@PathVariable (name ="id") Long id){
         var product = productRepository.findById(id).orElse(null);
         if(product==null){
             return ResponseEntity.notFound().build();
         }
         productRepository.delete(product);
         return ResponseEntity.noContent().build();
     }
}
