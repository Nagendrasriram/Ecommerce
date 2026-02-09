package com.ecommerce.backend.product;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productRepo.save(product);
    }

    @GetMapping
    public List<Product> getAll() {
        return productRepo.findAll();
    }
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id)
    {
        return productRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found"));
    }
    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody Product updatedProduct)
    {
        Product product = productRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Prodcut not found"));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());
        product.setActive(updatedProduct.isActive);
        return productRepo.save(product);
    }
    )
}
