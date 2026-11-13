package com.minierp.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for creating a new sale via REST or form submission.
 */
public class SaleRequest {

    private Long customerId;
    private List<SaleItemRequest> items;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public List<SaleItemRequest> getItems() { return items; }
    public void setItems(List<SaleItemRequest> items) { this.items = items; }

    // ------------------------------------------------------------------ //
    // Inner class for each line item                                      //
    // ------------------------------------------------------------------ //
    public static class SaleItemRequest {
        private Long productId;
        private int quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
