package org.example.products_manager.repository;

import org.example.products_manager.model.Product;
import org.example.products_manager.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This interface provides methods for performing CRUD operations on the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByBarcode(String barcode);

    // JPQL query to find tags in product_tags table by product ID
    @Query("SELECT t FROM Tag t JOIN ProductTag pt ON t.id = pt.tag.id WHERE pt.product.id = :productId")
    List<Tag> findTagsByProductId(@Param("productId") Long productId);

    // Native SQL query to insert a record into product_tags table
    @Modifying
    @Query(value = "INSERT INTO product_tags (product_id, tag_id) VALUES (:productId, :tagId)", nativeQuery = true)
    void saveTag(@Param("productId") Long productId, @Param("tagId") Long tagId);
}
