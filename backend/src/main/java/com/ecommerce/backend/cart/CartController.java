package com.ecommerce.backend.cart;

import com.ecommerce.backend.product.Product;
import com.ecommerce.backend.product.ProductRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ProductRepository productRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;

    public CartController(
            CartRepository cartRepo,
            CartItemRepository cartItemRepo,
            ProductRepository productRepo
    ) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
    }

    // ✅ View Cart
    @GetMapping
    public List<CartItem> viewCart(@RequestParam Long userid) {

        Cart cart = cartRepo.findByUserid(userid)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserid(userid);
                    return cartRepo.save(newCart);
                });

        return cartItemRepo.findByCartId(cart.getId());
    }

    // ✅ Add Item to Cart
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam Long userid,
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getActive()) {
            throw new RuntimeException("Product is inactive");
        }

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        Cart cart = cartRepo.findByUserid(userid)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserid(userid);
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

        return ResponseEntity.ok(
                Map.of(
                        "message", "Item added to cart successfully",
                        "productId", productId,
                        "quantity", item.getQuantity()
                )
        );
    }

    // ✅ Cart Total
    @GetMapping("/total")
    public Double getCartTotal(@RequestParam Long userid) {

        Cart cart = cartRepo.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cartItemRepo.findByCartId(cart.getId());

        double total = 0;
        for (CartItem item : items) {
            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            total += product.getPrice().doubleValue() * item.getQuantity();
        }
        return total;
    }
}
