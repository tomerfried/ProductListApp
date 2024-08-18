package org.example.products_manager;

import org.example.products_manager.controller.ProductsManagerController;
import org.example.products_manager.exception.InvalidBarcodeException;
import org.example.products_manager.exception.InvalidProductRequestException;
import org.example.products_manager.model.Product;
import org.example.products_manager.model.ProductRequest;
import org.example.products_manager.model.ProductResponse;
import org.example.products_manager.service.ProductsManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Each test method in this class corresponds to a method in the ProductsManagerController.
 * Mostly tested: controller's response to valid inputs, as well as its error handling capabilities when given invalid inputs.
 * productsManagerService is mocked to isolate the testing to the controller only.
 */
public class ProductsManagerControllerTests {

    @InjectMocks
    private ProductsManagerController productsManagerController;

    @Mock
    private ProductsManagerService productsManagerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        ProductRequest productRequest = new ProductRequest("123456", "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f, Arrays.asList("tag1", "tag2"));
        ProductResponse productResponse = new ProductResponse("123456", "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f, Arrays.asList("tag1", "tag2"));

        when(productsManagerService.createProduct(productRequest)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productsManagerController.createProduct(productRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productResponse, response.getBody());
    }

    @Test
    public void testGetProduct() {
        String barcode = "123456";
        ProductResponse productResponse = new ProductResponse("123456", "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f, Arrays.asList("tag1", "tag2"));


        when(productsManagerService.getProduct(barcode)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productsManagerController.getProduct(barcode);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productResponse, response.getBody());
    }

    @Test
    public void testGetAllProducts() {
        List<ProductResponse> productResponses = new ArrayList<>();

        ProductResponse product1 = new ProductResponse("123456", "Test Product 1", "http://example.com/image1.jpg", 4.5f, 19.99f, Arrays.asList("tag1", "tag2"));
        productResponses.add(product1);

        ProductResponse product2 = new ProductResponse("789012", "Test Product 2", "http://example.com/image2.jpg", 4.0f, 15.99f, Arrays.asList("tag3", "tag4"));
        productResponses.add(product2);

        when(productsManagerService.getAllProducts("id")).thenReturn(productResponses);
        ResponseEntity<List<ProductResponse>> response = productsManagerController.getAllProducts("id");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productResponses, response.getBody());
    }

    @Test
    public void testUpdateProduct() {
        String barcode = "123456";

        ProductRequest productRequest = new ProductRequest("123456", "Updated Product", "http://example.com/image2.jpg", 4.0f, 15.99f, Arrays.asList("tag3", "tag4"));
        ProductResponse productResponse = new ProductResponse("123456", "Updated Product", "http://example.com/image2.jpg", 4.0f, 15.99f, Arrays.asList("tag3", "tag4"));

        when(productsManagerService.updateProduct(barcode, productRequest)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productsManagerController.updateProduct(barcode, productRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productResponse, response.getBody());
    }

    @Test
    public void testDeleteProduct() {
        String barcode = "123456";

        ResponseEntity<String> response = productsManagerController.deleteProduct(barcode);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Product with barcode " + barcode + " was deleted", response.getBody());
    }

    @Test
    public void testCreateProductInvalidBarcode() {
        ProductRequest productRequest = new ProductRequest("abc", "Test Product", "http://example.com/image.jpg", 4.5f, 19.99f, Arrays.asList("tag1", "tag2"));

        assertThrows(InvalidProductRequestException.class, () -> {
            productsManagerController.createProduct(productRequest);
        });
    }

    @Test
    public void testGetProductInvalidBarcode() {
        String barcode = "abc";

        assertThrows(InvalidBarcodeException.class, () -> {
            productsManagerController.getProduct(barcode);
        });
    }

    @Test
    public void testDeleteProductInvalidBarcode() {
        String barcode = "abc";

        assertThrows(InvalidBarcodeException.class, () -> {
            productsManagerController.deleteProduct(barcode);
        });
    }

    @Test
    public void testUpdateProductInvalidBarcode_shouldThrowInvalidBarcodeException() {
        String invalidBarcode = "abc123";

        ProductRequest productRequest = new ProductRequest("123456", "Updated Product", "http://example.com/image2.jpg", 4.0f, 15.99f, Arrays.asList("tag3", "tag4"));

        assertThrows(InvalidBarcodeException.class, () -> {
            productsManagerController.updateProduct(invalidBarcode, productRequest);
        });
    }

    @Test
    public void testUpdateProductMissingBarcode_shouldThrowInvalidBarcodeException() {
        String invalidBarcode = "";

        ProductRequest productRequest = new ProductRequest("123456", "Updated Product", "http://example.com/image2.jpg", 4.0f, 15.99f, Arrays.asList("tag3", "tag4"));

        assertThrows(InvalidBarcodeException.class, () -> {
            productsManagerController.updateProduct(invalidBarcode, productRequest);
        });
    }

    @Test
    public void testUpdateProductNullBarcode_shouldThrowInvalidBarcodeException() {
        String invalidBarcode = null;

        ProductRequest productRequest = new ProductRequest("123456", "Updated Product", "http://example.com/image2.jpg", 4.0f, 15.99f, Arrays.asList("tag3", "tag4"));

        assertThrows(InvalidBarcodeException.class, () -> {
            productsManagerController.updateProduct(invalidBarcode, productRequest);
        });
    }

    @Test
    public void testUpdateProductInvalidTag_shouldThrowInvalidProductRequestException() {
        String validBarcode = "123456";
        ProductRequest productRequest = new ProductRequest();
        productRequest.setBarcode(validBarcode);
        productRequest.setName("hello");
        productRequest.setTags(Arrays.asList("tag1", "")); // Invalid tag

        assertThrows(InvalidProductRequestException.class, () -> {
            productsManagerController.updateProduct(validBarcode, productRequest);
        });
    }

    @Test
    public void testUpdateProductMissingName_shouldThrowInvalidProductRequestException() {
        String validBarcode = "123456";

        ProductRequest productRequest = new ProductRequest();
        productRequest.setBarcode(validBarcode);
        productRequest.setName("");

        assertThrows(InvalidProductRequestException.class, () -> {
            productsManagerController.updateProduct(validBarcode, productRequest);
        });
    }

    @Test
    public void testUpdateProductInvalidImageURL_shouldThrowInvalidProductRequestException() {
        String validBarcode = "123456";

        ProductRequest productRequest = new ProductRequest();
        productRequest.setBarcode(validBarcode);
        productRequest.setName("Updated Product");
        productRequest.setImage("invalid-url");
        assertThrows(InvalidProductRequestException.class, () -> {
            productsManagerController.updateProduct(validBarcode, productRequest);
        });
    }
}