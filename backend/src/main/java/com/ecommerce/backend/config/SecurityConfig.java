package com.ecommerce.backend.config;

import com.ecommerce.backend.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private  final JwtFilter jwtFilter;
    public SecurityConfig(JwtFilter jwtFilter)
    {
        this.jwtFilter = jwtFilter;
    }
    // 🔐 Password hashing bean (keep this)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔓 Security rules (THIS FIXES 401)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // APIs don’t need CSRF

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // allow register/login
                        .anyRequest().authenticated()
                )

//                .httpBasic(withDefaults()); // temporary auth (we’ll replace with JWT)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
