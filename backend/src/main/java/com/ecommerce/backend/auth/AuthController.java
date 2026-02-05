package com.ecommerce.backend.auth;

import com.ecommerce.backend.user.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user)
    {
        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                      @RequestParam String password)
    {
        return authService.login(email, password);
    }
}
