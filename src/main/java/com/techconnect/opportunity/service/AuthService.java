package com.techconnect.opportunity.service;

import com.techconnect.opportunity.dto.AuthRequest;
import com.techconnect.opportunity.dto.AuthResponse;
import com.techconnect.opportunity.model.User;
import com.techconnect.opportunity.repository.UserRepository;
import com.techconnect.opportunity.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository repository, JwtUtil jwtUtil) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AuthResponse register(AuthRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User(request.username(), passwordEncoder.encode(request.password()));
        user.setRoles(Set.of("USER"));
        repository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), "USER");
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        User user = repository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRoles().iterator().next());
        return new AuthResponse(token);
    }
}
