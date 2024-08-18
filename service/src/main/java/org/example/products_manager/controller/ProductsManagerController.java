package org.example.products_manager.controller;

import org.example.products_manager.exception.InvalidBarcodeException;
import org.example.products_manager.exception.InvalidProductRequestException;
import org.example.products_manager.model.ProductRequest;
import org.example.products_manager.model.ProductResponse;
import org.example.products_manager.service.ProductsManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Controller for managing products in the database
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/products")
public class ProductsManagerController {

    private static final Logger logger = LoggerFactory.getLogger(ProductsManagerController.class);

    @Autowired
    private ProductsManagerService productsManagerService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        validateProductRequest(productRequest);
        ProductResponse productResponse = productsManagerService.createProduct(productRequest);
        logger.info("Product created successfully: {}", productResponse);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{barcode}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String barcode) {
        validateBarcode(barcode);
        ProductResponse productResponse = productsManagerService.getProduct(barcode);
        logger.info("Product retrieved successfully: {}", productResponse);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }
        List<ProductResponse> productResponses = productsManagerService.getAllProducts(sortBy);
        logger.info("All products retrieved successfully");
        return ResponseEntity.ok(productResponses);
    }

    @PatchMapping("/{barcode}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String barcode,
            @RequestBody ProductRequest productRequest) {
        validateBarcode(barcode);
        validateProductRequest(productRequest);
        ProductResponse productResponse = productsManagerService.updateProduct(barcode, productRequest);
        logger.info("Product updated successfully: {}", productResponse);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{barcode}")
    public ResponseEntity<String> deleteProduct(@PathVariable String barcode) {
        validateBarcode(barcode);
        productsManagerService.deleteProduct(barcode);
        logger.info("Product with barcode {} was deleted", barcode);
        return ResponseEntity.ok("Product with barcode " + barcode + " was deleted");
    }

    /**
     * Ensures the requested product has valid values in its fields.
     *
     * @param productRequest the product request
     */
    private static void validateProductRequest(ProductRequest productRequest) {
        if (productRequest.getBarcode() == null || productRequest.getBarcode().trim().isEmpty()) {
            throw new InvalidProductRequestException("Barcode is mandatory");
        }
        if (!productRequest.getBarcode().matches("[0-9]+")) {
            throw new InvalidProductRequestException("Barcode can only contain digits");
        }
        if (productRequest.getName() == null || productRequest.getName().trim().isEmpty()) {
            throw new InvalidProductRequestException("Name is mandatory");
        }
        if (productRequest.getImage() != null && !productRequest.getImage().trim().isEmpty()) {
            try {
                new URL(productRequest.getImage());
            } catch (MalformedURLException e) {
                throw new InvalidProductRequestException("Image must be a valid URL or empty");
            }
        }
        for (String tag : productRequest.getTags()) {
            if (tag == null || tag.trim().isEmpty()) {
                throw new InvalidProductRequestException("Tag cannot be blank");
            }
        }
    }

    /**
     * Ensures the barcode is valid.
     *
     * @param barcode the barcode
     */
    private static void validateBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            throw new InvalidBarcodeException("Barcode is mandatory");
        }
        if (!barcode.matches("[0-9]+")) {
            throw new InvalidBarcodeException("Barcode can only contain digits");
        }
    }
}