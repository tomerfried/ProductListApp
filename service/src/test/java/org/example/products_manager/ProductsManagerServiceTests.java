package org.example.products_manager;

import org.example.products_manager.exception.BarcodeAlreadyExistsException;
import org.example.products_manager.exception.DatabaseAccessException;
import org.example.products_manager.exception.ProductNotFoundException;
import org.example.products_manager.model.Product;
import org.example.products_manager.model.ProductRequest;
import org.example.products_manager.model.ProductResponse;
import org.example.products_manager.model.Tag;
import org.example.products_manager.repository.ProductRepository;
import org.example.products_manager.repository.ProductTagRepository;
import org.example.products_manager.repository.TagRepository;
import org.example.products_manager.service.ProductsManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Each test method in this class corresponds to a method in the ProductsManagerService.
 * Mostly tested: service's response to valid inputs, error handling capabilities when given invalid inputs
 * non-existing products in the database, or when a database error occurs.
 * The service's interaction with the repositories is mocked to isolate the testing to the service only.
 */
public class ProductsManagerServiceTests {

    @InjectMocks
    private ProductsManagerService productsManagerService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductTagRepository productTagRepository;

    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        ProductRequest productRequest = new ProductRequest("123456", "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f, Arrays.asList("tag1", "tag2"));
        Product product = new Product(1L, "123456", "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f);

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(tagRepository.findByTagName(anyString())).thenReturn(null);
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag());

        ProductResponse response = productsManagerService.createProduct(productRequest);

        assertEquals("123456", response.getBarcode());
        assertEquals("Test Product", response.getName());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(tagRepository, times(2)).save(any(Tag.class));
    }

    @Test
    public void testCreateProduct_BarcodeAlreadyExists() {
        ProductRequest productRequest = new ProductRequest("123456", "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f, Arrays.asList("tag1", "tag2"));
        Product existingProduct = new Product(1L, "123456", "Existing Product", "http://example.com/image.jpg", 4.5f, 19.99f);

        when(productRepository.findByBarcode(productRequest.getBarcode())).thenReturn(Optional.of(existingProduct));

        assertThrows(BarcodeAlreadyExistsException.class, () -> {
            productsManagerService.createProduct(productRequest);
        });
    }

    @Test
    public void testCreateProduct_DatabaseError() {
        ProductRequest productRequest = new ProductRequest("123456", "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f, Arrays.asList("tag1", "tag2"));

        when(productRepository.save(any(Product.class))).thenThrow(JpaSystemException.class);

        assertThrows(DatabaseAccessException.class, () -> {
            productsManagerService.createProduct(productRequest);
        });
    }

    @Test
    public void testGetProduct_Success() {
        Product product = new Product(1L, "123456", "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f);

        when(productRepository.findByBarcode("123456")).thenReturn(Optional.of(product));
        when(productRepository.findTagsByProductId(product.getId())).thenReturn(Arrays.asList(new Tag(1L, "tag1"), new Tag(2L, "tag2")));

        ProductResponse response = productsManagerService.getProduct("123456");

        assertEquals("123456", response.getBarcode());
        assertEquals("Test Product", response.getName());
        assertEquals("http://example.com/image.jpg", response.getImage());
        assertEquals(4.5f, response.getRating());
        assertEquals(19.99f, response.getPrice());
        assertEquals(Arrays.asList("tag1", "tag2"), response.getTags());
    }

    @Test
    public void testGetProduct_NotFound() {
        String barcode = "123456";
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productsManagerService.getProduct(barcode);
        });
    }

    @Test
    public void testGetProduct_DatabaseError() {
        String barcode = "123456";
        when(productRepository.findByBarcode(barcode)).thenThrow(JpaSystemException.class);

        assertThrows(DatabaseAccessException.class, () -> {
            productsManagerService.getProduct(barcode);
        });
    }

    @Test
    public void testGetAllProducts_DatabaseError() {
        Sort sortMethod = Sort.by(Sort.Direction.ASC, "id");
        when(productRepository.findAll(sortMethod)).thenThrow(JpaSystemException.class);

        assertThrows(DatabaseAccessException.class, () -> {
            productsManagerService.getAllProducts("id");
        });
    }

    @Test
    public void testUpdateProduct_Success() {
        String oldBarcode = "123456";
        String newBarcode = "654321";
        ProductRequest productRequest = new ProductRequest(newBarcode, "Updated Product", "http://example.com/image2.jpg", 4.0f, 15.99f, Arrays.asList("tag3", "tag4"));
        Product product = new Product(1L, oldBarcode, "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f);

        when(productRepository.findByBarcode(oldBarcode)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productRepository.findTagsByProductId(product.getId())).thenReturn(Arrays.asList(new Tag(1L, "tag3"), new Tag(2L, "tag4")));
        when(tagRepository.findByTagName(anyString())).thenReturn(null);
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag(1L, "tag3"), new Tag(2L, "tag4"));

        ProductResponse response = productsManagerService.updateProduct(oldBarcode, productRequest);

        assertEquals(newBarcode, response.getBarcode());
        assertEquals("Updated Product", response.getName());
        assertEquals("http://example.com/image2.jpg", response.getImage());
        assertEquals(4.0f, response.getRating());
        assertEquals(15.99f, response.getPrice());
        assertEquals(Arrays.asList("tag3", "tag4"), response.getTags());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(tagRepository, times(2)).save(any(Tag.class));
    }


    @Test
    public void testUpdateProduct_NotFound() {
        String barcode = "123456";
        ProductRequest productRequest = new ProductRequest();

        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productsManagerService.updateProduct(barcode, productRequest);
        });
    }

    @Test
    public void testUpdateProduct_DatabaseError() {
        String barcode = "123456";
        ProductRequest productRequest = new ProductRequest();

        when(productRepository.findByBarcode(barcode)).thenThrow(JpaSystemException.class);

        assertThrows(DatabaseAccessException.class, () -> {
            productsManagerService.updateProduct(barcode, productRequest);
        });
    }

    @Test
    public void testDeleteProduct_Success() {
        String barcode = "123456";
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.of(product));
        doNothing().when(productTagRepository).deleteAll(anyList());
        doNothing().when(productRepository).delete(product);

        productsManagerService.deleteProduct(barcode);

        verify(productTagRepository, times(1)).deleteAll(anyList());
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        String barcode = "123456";

        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productsManagerService.deleteProduct(barcode);
        });
    }

    @Test
    public void testDeleteProduct_DatabaseError() {
        String barcode = "123456";

        when(productRepository.findByBarcode(barcode)).thenThrow(JpaSystemException.class);

        assertThrows(DatabaseAccessException.class, () -> {
            productsManagerService.deleteProduct(barcode);
        });
    }
}
