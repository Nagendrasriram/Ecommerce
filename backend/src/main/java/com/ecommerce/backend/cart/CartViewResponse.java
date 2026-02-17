package com.ecommerce.backend.cart;

import java.math.BigDecimal;

public class CartViewResponse {

    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;

    public CartViewResponse(
            Long productId,
            String productName,
            BigDecimal price,
            Integer quantity,
            BigDecimal subtotal
    ) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getPrice() { return price; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
}
