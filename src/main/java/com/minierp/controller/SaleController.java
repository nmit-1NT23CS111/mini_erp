package com.minierp.controller;

import com.minierp.dto.SaleRequest;
import com.minierp.exception.InsufficientStockException;
import com.minierp.exception.ResourceNotFoundException;
import com.minierp.model.Customer;
import com.minierp.model.Product;
import com.minierp.model.Sale;
import com.minierp.service.CustomerService;
import com.minierp.service.ProductService;
import com.minierp.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sales")
public class SaleController {

    @Autowired private SaleService saleService;
    @Autowired private CustomerService customerService;
    @Autowired private ProductService productService;

    // ------------------------------------------------------------------ //
    // Sales History                                                       //
    // ------------------------------------------------------------------ //
    @GetMapping
    public String listSales(Model model) {
        model.addAttribute("sales", saleService.getAllSales());
        return "sales/list";
    }

    // ------------------------------------------------------------------ //
    // Create Sale form                                                    //
    // ------------------------------------------------------------------ //
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        return "sales/create";
    }

    // ------------------------------------------------------------------ //
    // Submit Sale                                                         //
    // ------------------------------------------------------------------ //
    @PostMapping("/save")
    public String saveSale(@RequestParam Long customerId,
                           @RequestParam List<Long> productIds,
                           @RequestParam List<Integer> quantities,
                           RedirectAttributes ra) {
        try {
            SaleRequest request = new SaleRequest();
            request.setCustomerId(customerId);

            List<SaleRequest.SaleItemRequest> items = new ArrayList<>();
            for (int i = 0; i < productIds.size(); i++) {
                // Skip empty rows (product id == 0 or quantity == 0)
                if (productIds.get(i) == null || productIds.get(i) == 0) continue;
                if (quantities.get(i) == null || quantities.get(i) <= 0) continue;

                SaleRequest.SaleItemRequest item = new SaleRequest.SaleItemRequest();
                item.setProductId(productIds.get(i));
                item.setQuantity(quantities.get(i));
                items.add(item);
            }
            request.setItems(items);

            Sale sale = saleService.createSale(request);
            ra.addFlashAttribute("success", "Sale #" + sale.getId() + " created successfully!");
            return "redirect:/sales/" + sale.getId();
        } catch (InsufficientStockException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to create sale: " + e.getMessage());
        }
        return "redirect:/sales/create";
    }

    // ------------------------------------------------------------------ //
    // Sale Details                                                        //
    // ------------------------------------------------------------------ //
    @GetMapping("/{id}")
    public String saleDetails(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Sale sale = saleService.getSaleById(id);
            model.addAttribute("sale", sale);
            return "sales/details";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/sales";
        }
    }
}
