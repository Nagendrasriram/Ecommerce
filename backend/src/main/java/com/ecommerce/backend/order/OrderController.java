package com.ecommerce.backend.order;

import com.ecommerce.backend.cart.CartItem;
import com.ecommerce.backend.cart.CartItemRepository;
import com.ecommerce.backend.cart.CartRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;   // ✅ FIXED TYPE

    public OrderController(
            OrderRepository orderRepo,
            CartRepository cartRepo,
            CartItemRepository cartItemRepo   // ✅ FIXED TYPE
    ) {
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }

    // ✅ Place Order
    @PostMapping("/place")
    public String placeOrder(@RequestParam Long userid) {

        var cart = cartRepo.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cartItemRepo.findByCartId(cart.getId());

        if (items.isEmpty()) {
            return "Cart is empty";
        }

        double total = items.stream()
                .mapToDouble(i -> i.getQuantity() * 1000) // temp price logic
                .sum();

        Order order = new Order();
        order.setUserid(userid);
        order.setTotalAmount(total);

        orderRepo.save(order);

        // clear cart
        cartItemRepo.deleteAll(items);

        return "Order placed successfully";
    }

    // ✅ View Orders
    @GetMapping
    public List<Order> getOrders(@RequestParam Long userid) {
        return orderRepo.findByUserid(userid);
    }
}
