package com.idea.voting.controller;
import java.util.HashMap;
import java.util.Map;

import com.idea.voting.dto.IdeaDTO;
import com.idea.voting.model.User;
import com.idea.voting.service.UserService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register user
    @PostMapping("/register")
    public ResponseEntity<IdeaDTO.UserSummary> register(@RequestBody Map<String, String> body) {

        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");

        if (username == null || email == null || password == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.register(username, email, password);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.toSummary(user));
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");

        boolean authenticated = userService.authenticate(username, password);

        if (authenticated) {
            Map<String, String> response = new HashMap<>();
            response.put("token", "dummy-token");
            response.put("username", username);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid username or password"));
    }

    // Get all users (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<IdeaDTO.UserSummary>> getAllUsers() {

        List<IdeaDTO.UserSummary> users = userService.findAll()
                .stream()
                .map(userService::toSummary)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // Get user by id (ADMIN only)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IdeaDTO.UserSummary> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                userService.toSummary(userService.findById(id))
        );
    }

    // Delete user (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}