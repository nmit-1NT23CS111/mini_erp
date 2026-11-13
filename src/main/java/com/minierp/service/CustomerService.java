package com.minierp.service;

import com.minierp.model.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    List<Customer> searchCustomers(String keyword);
    Customer getCustomerById(Long id);
    Long addCustomer(Customer customer);
    void updateCustomer(Customer customer);
}
