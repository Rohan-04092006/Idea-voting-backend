package com.idea.voting.controller;

import com.idea.voting.dto.IdeaDTO;
import com.idea.voting.model.Idea;
import com.idea.voting.service.IdeaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ideas")
@CrossOrigin(origins = "https://idea-voting-backend.onrender.com")
public class IdeaController {

    private final IdeaService ideaService;

    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    // POST /api/ideas
    @PostMapping
    public ResponseEntity<IdeaDTO.Response> create(
            @RequestBody IdeaDTO.CreateRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = principal.getUsername();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ideaService.create(request, username));
    }

    // GET /api/ideas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<IdeaDTO.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ideaService.findById(id));
    }

    // GET /api/ideas
    @GetMapping
    public ResponseEntity<Page<IdeaDTO.Response>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort) {

        return ResponseEntity.ok(ideaService.findAll(page, size, sort));
    }

    // GET /api/ideas/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<IdeaDTO.Response>> getByStatus(
            @PathVariable Idea.Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ideaService.findByStatus(status, page, size));
    }

    // GET /api/ideas/author/{authorId}
    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<IdeaDTO.Response>> getByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ideaService.findByAuthor(authorId, page, size));
    }

    // SEARCH ideas
    @GetMapping("/search")
    public ResponseEntity<Page<IdeaDTO.Response>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ideaService.search(keyword, page, size));
    }

    // TOP ideas
    @GetMapping("/top")
    public ResponseEntity<List<IdeaDTO.Response>> getTop(
            @RequestParam(defaultValue = "5") int limit) {

        return ResponseEntity.ok(ideaService.findTopIdeas(limit));
    }

    // UPDATE idea
    @PutMapping("/{id}")
    public ResponseEntity<IdeaDTO.Response> update(
            @PathVariable Long id,
            @Valid @RequestBody IdeaDTO.UpdateRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
                ideaService.update(id, request, principal.getUsername()));
    }

    // DELETE idea
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ideaService.delete(id, principal.getUsername());

        return ResponseEntity.noContent().build();
    }
}