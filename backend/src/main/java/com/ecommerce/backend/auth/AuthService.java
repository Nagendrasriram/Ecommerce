package com.ecommerce.backend.auth;
import com.ecommerce.backend.security.JwtService;
import com.ecommerce.backend.user.*;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepo, BCryptPasswordEncoder encoder, JwtService jwtService)
    {
        this.userRepo = userRepo;
        this.encoder= encoder;
        this.jwtService = jwtService;
    }
    public User register(User user) {

        // Hash password before saving
        user.setPassword(encoder.encode(user.getPassword()));

        // default role
        user.setRole(Role.USER);

        return userRepo.save(user);
    }
    // LOGIN USER
    public String login(String email, String password) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check raw password vs hashed password
        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
