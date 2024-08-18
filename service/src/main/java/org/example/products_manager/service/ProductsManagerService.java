package org.example.products_manager.service;

import org.example.products_manager.exception.BarcodeAlreadyExistsException;
import org.example.products_manager.exception.DatabaseAccessException;
import org.example.products_manager.exception.ProductNotFoundException;
import org.example.products_manager.model.*;
import org.example.products_manager.repository.ProductRepository;
import org.example.products_manager.repository.ProductTagRepository;
import org.example.products_manager.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service class for managing products.
 * This class provides methods for creating, retrieving, updating, and deleting products.
 * It also handles the association of tags with products.
 */
@Service
public class ProductsManagerService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private TagRepository tagRepository;

    /**
     * Creates a new product based on the provided request.
     * Also handles the association of tags with the product.
     *
     * @param productRequest the request containing the product details
     * @return the response containing the created product details
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        try {
            if (productRepository.findByBarcode(productRequest.getBarcode()).isPresent()) {
                throw new BarcodeAlreadyExistsException("Product with barcode " + productRequest.getBarcode() + " already exists");
            }
            Product product = new Product();
            product.setBarcode(productRequest.getBarcode());
            product.setName(productRequest.getName());
            product.setImage(productRequest.getImage());
            product.setRating(productRequest.getRating());
            product.setPrice(productRequest.getPrice());
            product = productRepository.save(product);

            handleTags(product, productRequest.getTags(), false);

            return convertToProductResponse(product);
        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database");
        }

    }

    /**
     * Retrieves a product by its barcode.
     *
     * @param barcode the barcode of the product
     * @return the response containing the product details
     * @throws ProductNotFoundException if the product is not found
     * @throws DatabaseAccessException  if there is an error accessing the database
     */
    public ProductResponse getProduct(String barcode) {
        Product product;
        try {
            product = productRepository.findByBarcode(barcode)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with barcode: " + barcode));
            return convertToProductResponse(product);
        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    /**
     * Retrieves all products from the database, sorted by the specified field.
     *
     * @param sortBy the field by which to sort the products. If null, products are sorted by their id.
     * @return a list of ProductResponse objects, each representing a product in the database.
     * @throws DatabaseAccessException if there is an error accessing the database.
     */
    public List<ProductResponse> getAllProducts(String sortBy) {
        try {
            Sort sortMethod = Sort.by(Sort.Direction.ASC, Objects.requireNonNullElse(sortBy, "id"));
            List<Product> allProducts = productRepository.findAll(sortMethod);
            return allProducts.stream().map(this::convertToProductResponse).collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    /**
     * Updates a product based on the provided request.
     * Also handles the association of tags with the product.
     *
     * @param barcode        the barcode of the product to update
     * @param productRequest the request containing the new product details
     * @return the response containing the updated product details
     * @throws ProductNotFoundException if the product is not found
     * @throws DatabaseAccessException  if there is an error accessing the database
     */
    @Transactional
    public ProductResponse updateProduct(String barcode, ProductRequest productRequest) {
        Product product;
        try {
            product = productRepository.findByBarcode(barcode)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with barcode: " + barcode));

            if (productRequest.getBarcode() != null) {
                product.setBarcode(productRequest.getBarcode());
            }

            if (productRequest.getName() != null) {
                product.setName(productRequest.getName());
            }

            if (productRequest.getImage() != null) {
                product.setImage(productRequest.getImage());
            }

            if (productRequest.getRating() != null) {
                product.setRating(productRequest.getRating());
            }

            if (productRequest.getPrice() != null) {
                product.setPrice(productRequest.getPrice());
            }

            if (productRequest.getTags() != null) {
                handleTags(product, productRequest.getTags(), true);
            }

            product = productRepository.save(product);

            return convertToProductResponse(product);

        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    /**
     * Deletes a product by its barcode.
     * Also handles the removal of the association of tags with the product.
     *
     * @param barcode the barcode of the product to delete
     * @throws ProductNotFoundException if the product is not found
     * @throws DatabaseAccessException  if there is an error accessing the database
     */
    @Transactional
    public void deleteProduct(String barcode) {
        Product product;
        try {
            product = productRepository.findByBarcode(barcode)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with barcode: " + barcode));

            List<ProductTag> productTags = productTagRepository.findByProductId(product.getId());
            productTagRepository.deleteAll(productTags);

            productRepository.delete(product);

        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    /**
     * Handles the association of tags with a product.
     *
     * This method associates the provided tags with the given product. If the product is an existing product,
     * it first removes all existing tag associations before adding the new ones. If a tag does not exist in the database,
     * it creates a new tag.
     *
     * @param product the product to associate tags with
     * @param tagNames the list of tag names to associate with the product
     * @param isExistingProduct a flag indicating whether the product is an existing product (true) or a new product (false)
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    private void handleTags(Product product, List<String> tagNames, boolean isExistingProduct) {
        List<ProductTag> productTags = productTagRepository.findByProductId(product.getId());

        if (isExistingProduct) {
            productTagRepository.deleteAll(productTags);
        }

        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByTagName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setTagName(tagName);
                tag = tagRepository.save(tag);
            }
            productRepository.saveTag(product.getId(), tag.getId());
        }
    }

    private ProductResponse convertToProductResponse(Product product) {
        List<Tag> tags = productRepository.findTagsByProductId(product.getId());
        List<String> tagNames = tags.stream().map(Tag::getTagName).collect(Collectors.toList());

        return new ProductResponse(
                product.getBarcode(),
                product.getName(),
                product.getImage(),
                product.getRating(),
                product.getPrice(),
                tagNames
        );
    }
}