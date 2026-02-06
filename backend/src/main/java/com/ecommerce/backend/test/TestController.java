package com.ecommerce.backend.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/secure")
    public String secureEndpoint() {
        return "JWT is working! You are authenticated 🎉";
    }
}
