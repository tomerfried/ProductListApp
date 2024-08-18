package org.example.products_manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Represents a product with details: id, barcode, name, image, tags, rating, and price.
 * Defines the scheme of the corresponding table in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;
    @Column(name = "name")
    private String name;
    @Column(name = "image")
    private String image;
    @Column(name = "rating")
    private Float rating;
    @Column(name = "price")
    private Float price;
}
