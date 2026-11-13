package com.minierp.dao;

import com.minierp.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> findAll();
    List<Customer> search(String keyword);
    Optional<Customer> findById(Long id);
    Long save(Customer customer);
    int update(Customer customer);
    boolean emailExists(String email, Long excludeId);
    long count();
}
