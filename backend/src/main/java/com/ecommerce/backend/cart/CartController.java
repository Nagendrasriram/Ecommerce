package com.ecommerce.backend.cart;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;

    public CartController(CartRepository cartRepo, CartItemRepository cartItemRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }

    // 🔹 View cart
    @GetMapping
    public List<CartItem> viewCart(@RequestParam Long userId) {

        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setId(userId);
                    return cartRepo.save(newCart);
                });

        return cartItemRepo.findByCartId(cart.getId());
    }

    // 🔹 Add item to cart
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setId(userId);
                    return cartRepo.save(newCart);
                });

        CartItem item = cartItemRepo
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setCartId(cart.getId());
            item.setProductId(productId);
            item.setQuantity(quantity);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }

        cartItemRepo.save(item);

        return "Item added to cart";
    }
}
