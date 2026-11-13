package com.minierp.rest;

import com.minierp.dto.ApiResponse;
import com.minierp.dto.SaleRequest;
import com.minierp.exception.InsufficientStockException;
import com.minierp.exception.ResourceNotFoundException;
import com.minierp.model.Sale;
import com.minierp.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleRestController {

    @Autowired
    private SaleService saleService;

    /** GET /api/sales */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Sale>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(saleService.getAllSales()));
    }

    /** GET /api/sales/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Sale>> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(saleService.getSaleById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    /** POST /api/sales
     *  Body: { "customerId": 1, "items": [{"productId": 1, "quantity": 2}] }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createSale(@RequestBody SaleRequest request) {
        try {
            Sale sale = saleService.createSale(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sale created successfully.", sale.getId()));
        } catch (InsufficientStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
