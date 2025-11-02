package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Byte categoryId;
}
