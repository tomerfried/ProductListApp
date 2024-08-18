package org.example.products_manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entity class for a tag.
 * This class represents a tag that can be associated with products.
 * Each instance of this class represents a single unique tag.
 * Its scheme is defined by the corresponding table in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tag_name", unique = true, nullable = false)
    private String tagName;
}
