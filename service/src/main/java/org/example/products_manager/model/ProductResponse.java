package org.example.products_manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Response class for retrieving a product.
 * This class provides the necessary details for retrieving a product.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String barcode;
    private String name;
    private String image;
    private Float rating;
    private Float price;
    private List<String> tags;
}