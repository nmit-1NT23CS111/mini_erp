package com.minierp.service;

import com.minierp.dao.CustomerDao;
import com.minierp.exception.ResourceNotFoundException;
import com.minierp.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    // Basic email and phone patterns
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[+]?[0-9]{7,15}$");

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return customerDao.findAll();
        }
        return customerDao.search(keyword.trim());
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerDao.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }

    @Override
    public Long addCustomer(Customer customer) {
        validate(customer, null);
        return customerDao.save(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerDao.findById(customer.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer", customer.getId()));
        validate(customer, customer.getId());
        customerDao.update(customer);
    }

    // ------------------------------------------------------------------ //
    // Validation                                                          //
    // ------------------------------------------------------------------ //
    private void validate(Customer c, Long excludeId) {
        if (c.getName() == null || c.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        if (c.getEmail() != null && !c.getEmail().isBlank()) {
            if (!EMAIL_PATTERN.matcher(c.getEmail()).matches()) {
                throw new IllegalArgumentException("Invalid email format.");
            }
            if (customerDao.emailExists(c.getEmail(), excludeId)) {
                throw new IllegalArgumentException("A customer with this email already exists.");
            }
        }
        if (c.getPhone() != null && !c.getPhone().isBlank()) {
            String phone = c.getPhone().replaceAll("\\s+", "");
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                throw new IllegalArgumentException("Invalid phone number. Enter 7–15 digits.");
            }
        }
    }
}
