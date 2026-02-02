package com.ecommerce.backend.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query method
    // Spring auto converts this into SQL:
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

}
