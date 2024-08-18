package org.example.products_manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Request class for creating or updating a product.
 * This class provides the necessary details for creating or updating a product.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String barcode;
    private String name;
    private String image;
    private Float rating;
    private Float price;
    private List<String> tags;
}