package org.example.products_manager.repository;

import org.example.products_manager.model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface provides methods for performing CRUD operations on the Tag entity.
 */
@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    Tag findByTagName(String tagName);
}
