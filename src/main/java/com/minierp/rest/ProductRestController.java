package com.minierp.rest;

import com.minierp.dto.ApiResponse;
import com.minierp.exception.ResourceNotFoundException;
import com.minierp.model.Product;
import com.minierp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    /** GET /api/products  or  GET /api/products?search=laptop */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAll(
            @RequestParam(required = false) String search) {
        List<Product> products = search != null && !search.isBlank()
            ? productService.searchProducts(search)
            : productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.ok(products));
    }

    /** GET /api/products/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(productService.getProductById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    /** POST /api/products */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(@RequestBody Map<String, Object> body) {
        try {
            Product p = fromMap(body);
            Long id = productService.addProduct(p);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Product created.", id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** PUT /api/products/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id,
                                                     @RequestBody Map<String, Object> body) {
        try {
            Product p = fromMap(body);
            p.setId(id);
            productService.updateProduct(p);
            return ResponseEntity.ok(ApiResponse.ok("Product updated.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** DELETE /api/products/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.ok("Product deleted.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(e.getMessage()));
        }
    }

    private Product fromMap(Map<String, Object> body) {
        Product p = new Product();
        p.setName((String) body.get("name"));
        p.setDescription((String) body.getOrDefault("description", ""));
        Object price = body.get("price");
        if (price != null) {
            p.setPrice(new BigDecimal(price.toString()));
        }
        Object qty = body.get("quantity");
        if (qty != null) {
            p.setQuantity(Integer.parseInt(qty.toString()));
        }
        return p;
    }
}
