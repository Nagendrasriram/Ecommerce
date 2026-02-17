package com.ecommerce.backend.cart.dto;

import java.math.BigDecimal;

public class CartItemResponse {

    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;

    public CartItemResponse(
            Long productId,
            String name,
            BigDecimal price,
            Integer quantity
    ) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }

    // getters only (no setters needed)
    public Long getProductId() { return productId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
}
