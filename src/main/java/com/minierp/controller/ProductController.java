package com.minierp.controller;

import com.minierp.exception.ResourceNotFoundException;
import com.minierp.model.Product;
import com.minierp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ------------------------------------------------------------------ //
    // List products                                                       //
    // ------------------------------------------------------------------ //
    @GetMapping
    public String listProducts(@RequestParam(required = false) String search, Model model) {
        List<Product> products;
        if (search != null && !search.isBlank()) {
            products = productService.searchProducts(search);
            model.addAttribute("search", search);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        return "products/list";
    }

    // ------------------------------------------------------------------ //
    // Show Add form                                                       //
    // ------------------------------------------------------------------ //
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("formTitle", "Add New Product");
        model.addAttribute("action", "/products/save");
        return "products/form";
    }

    // ------------------------------------------------------------------ //
    // Save new product                                                    //
    // ------------------------------------------------------------------ //
    @PostMapping("/save")
    public String saveProduct(@RequestParam String name,
                              @RequestParam(required = false) String description,
                              @RequestParam BigDecimal price,
                              @RequestParam int quantity,
                              RedirectAttributes ra) {
        try {
            Product p = new Product();
            p.setName(name.trim());
            p.setDescription(description != null ? description.trim() : "");
            p.setPrice(price);
            p.setQuantity(quantity);
            productService.addProduct(p);
            ra.addFlashAttribute("success", "Product '" + name + "' added successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/products/add";
        }
        return "redirect:/products";
    }

    // ------------------------------------------------------------------ //
    // Show Edit form                                                      //
    // ------------------------------------------------------------------ //
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Product p = productService.getProductById(id);
            model.addAttribute("product", p);
            model.addAttribute("formTitle", "Edit Product");
            model.addAttribute("action", "/products/update/" + id);
            return "products/form";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/products";
        }
    }

    // ------------------------------------------------------------------ //
    // Update product                                                      //
    // ------------------------------------------------------------------ //
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam(required = false) String description,
                                @RequestParam BigDecimal price,
                                @RequestParam int quantity,
                                RedirectAttributes ra) {
        try {
            Product p = new Product();
            p.setId(id);
            p.setName(name.trim());
            p.setDescription(description != null ? description.trim() : "");
            p.setPrice(price);
            p.setQuantity(quantity);
            productService.updateProduct(p);
            ra.addFlashAttribute("success", "Product updated successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products";
    }

    // ------------------------------------------------------------------ //
    // Delete product                                                      //
    // ------------------------------------------------------------------ //
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        try {
            productService.deleteProduct(id);
            ra.addFlashAttribute("success", "Product deleted successfully.");
        } catch (IllegalStateException | ResourceNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products";
    }
}
