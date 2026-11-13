package com.minierp.controller;

import com.minierp.exception.ResourceNotFoundException;
import com.minierp.model.Customer;
import com.minierp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ------------------------------------------------------------------ //
    // List                                                                //
    // ------------------------------------------------------------------ //
    @GetMapping
    public String listCustomers(@RequestParam(required = false) String search, Model model) {
        List<Customer> customers;
        if (search != null && !search.isBlank()) {
            customers = customerService.searchCustomers(search);
            model.addAttribute("search", search);
        } else {
            customers = customerService.getAllCustomers();
        }
        model.addAttribute("customers", customers);
        return "customers/list";
    }

    // ------------------------------------------------------------------ //
    // Add form                                                            //
    // ------------------------------------------------------------------ //
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("formTitle", "Add New Customer");
        model.addAttribute("action", "/customers/save");
        return "customers/form";
    }

    // ------------------------------------------------------------------ //
    // Save                                                                //
    // ------------------------------------------------------------------ //
    @PostMapping("/save")
    public String saveCustomer(@RequestParam String name,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String address,
                               RedirectAttributes ra) {
        try {
            Customer c = new Customer();
            c.setName(name.trim());
            c.setEmail(email != null ? email.trim() : null);
            c.setPhone(phone != null ? phone.trim() : null);
            c.setAddress(address != null ? address.trim() : null);
            customerService.addCustomer(c);
            ra.addFlashAttribute("success", "Customer '" + name + "' added successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/customers/add";
        }
        return "redirect:/customers";
    }

    // ------------------------------------------------------------------ //
    // Edit form                                                           //
    // ------------------------------------------------------------------ //
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Customer c = customerService.getCustomerById(id);
            model.addAttribute("customer", c);
            model.addAttribute("formTitle", "Edit Customer");
            model.addAttribute("action", "/customers/update/" + id);
            return "customers/form";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/customers";
        }
    }

    // ------------------------------------------------------------------ //
    // Update                                                              //
    // ------------------------------------------------------------------ //
    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @RequestParam String name,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) String phone,
                                 @RequestParam(required = false) String address,
                                 RedirectAttributes ra) {
        try {
            Customer c = new Customer();
            c.setId(id);
            c.setName(name.trim());
            c.setEmail(email != null ? email.trim() : null);
            c.setPhone(phone != null ? phone.trim() : null);
            c.setAddress(address != null ? address.trim() : null);
            customerService.updateCustomer(c);
            ra.addFlashAttribute("success", "Customer updated successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/customers";
    }
}
