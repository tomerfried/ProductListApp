package org.example.products_manager.repository;

import org.example.products_manager.model.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface provides methods for performing CRUD operations on the ProductTag entity.
 */
public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
    List<ProductTag> findByProductId(Long productId);
}