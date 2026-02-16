package com.ecommerce.backend.order;

import com.ecommerce.backend.cart.*;
import com.ecommerce.backend.product.Product;
import com.ecommerce.backend.product.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;

    public OrderController(
            OrderRepository orderRepo,
            OrderItemRepository orderItemRepo,
            CartRepository cartRepo,
            CartItemRepository cartItemRepo,
            ProductRepository productRepo
    ) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
    }

    // ✅ PLACE ORDER
    @PostMapping("/place")
    public String placeOrder(@RequestParam Long userid) {

        Cart cart = cartRepo.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cartItemRepo.findByCartId(cart.getId());

        if (items.isEmpty()) {
            return "Cart is empty";
        }

        Order order = new Order();
        order.setUserid(userid);
        order = orderRepo.save(order);

        double total = 0;

        for (CartItem item : items) {
            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            double price = product.getPrice().doubleValue();
            total += price * item.getQuantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(price);

            orderItemRepo.save(orderItem);
        }

        order.setTotalAmount(total);
        orderRepo.save(order);

        cartItemRepo.deleteAll(items);

        return "Order placed successfully";
    }

    // ✅ VIEW ORDERS
    @GetMapping
    public List<Order> getOrders(@RequestParam Long userid) {
        return orderRepo.findByUserid(userid);
    }

    // ✅ VIEW ORDER ITEMS
    @GetMapping("/{orderId}/items")
    public List<OrderItem> getOrderItems(@PathVariable Long orderId) {
        return orderItemRepo.findByOrderId(orderId);
    }
}
