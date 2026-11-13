package com.minierp.rest;

import com.minierp.dto.ApiResponse;
import com.minierp.exception.ResourceNotFoundException;
import com.minierp.model.Customer;
import com.minierp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    /** GET /api/customers */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Customer>>> getAll(
            @RequestParam(required = false) String search) {
        List<Customer> customers = search != null && !search.isBlank()
            ? customerService.searchCustomers(search)
            : customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.ok(customers));
    }

    /** GET /api/customers/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(customerService.getCustomerById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    /** POST /api/customers */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(@RequestBody Map<String, String> body) {
        try {
            Customer c = fromMap(body);
            Long id = customerService.addCustomer(c);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Customer created.", id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** PUT /api/customers/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id,
                                                     @RequestBody Map<String, String> body) {
        try {
            Customer c = fromMap(body);
            c.setId(id);
            customerService.updateCustomer(c);
            return ResponseEntity.ok(ApiResponse.ok("Customer updated.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Customer fromMap(Map<String, String> body) {
        Customer c = new Customer();
        c.setName(body.get("name"));
        c.setEmail(body.get("email"));
        c.setPhone(body.get("phone"));
        c.setAddress(body.get("address"));
        return c;
    }
}
