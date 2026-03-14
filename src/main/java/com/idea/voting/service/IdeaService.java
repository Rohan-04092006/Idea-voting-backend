package com.idea.voting.service;

import com.idea.voting.dto.IdeaDTO;
import com.idea.voting.model.Idea;
import com.idea.voting.model.User;
import com.idea.voting.repository.IdeaRepository;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service

@Transactional
public class IdeaService {

    private  IdeaRepository ideaRepository;
    
    private  UserService userService;
    public IdeaService(IdeaRepository ideaRepository, UserService userService) {
        this.ideaRepository = ideaRepository;
        this.userService = userService;
    }
    private static final Logger log = LoggerFactory.getLogger(IdeaService.class);

    public IdeaDTO.Response create(IdeaDTO.CreateRequest request, String authorUsername) {
        User author = userService.findByUsername(authorUsername);

        Idea idea = new Idea();
        idea.setTitle(request.getTitle());
        idea.setDescription(request.getDescription());
        idea.setStatus(Idea.Status.OPEN);
        idea.setAuthor(author);

        Idea saved = ideaRepository.save(idea);
        log.info("Created idea id={} by user={}", saved.getId(), authorUsername);
        return IdeaDTO.Response.from(saved);
    }

    @Transactional(readOnly = true)
    public IdeaDTO.Response findById(Long id) {
        return IdeaDTO.Response.from(getIdeaOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<IdeaDTO.Response> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return ideaRepository.findAll(pageable).map(IdeaDTO.Response::from);
    }

    @Transactional(readOnly = true)
    public Page<IdeaDTO.Response> findByStatus(Idea.Status status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ideaRepository.findByStatus(status, pageable).map(IdeaDTO.Response::from);
    }

    @Transactional(readOnly = true)
    public Page<IdeaDTO.Response> findByAuthor(Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ideaRepository.findByAuthorId(authorId, pageable).map(IdeaDTO.Response::from);
    }

    @Transactional(readOnly = true)
    public Page<IdeaDTO.Response> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ideaRepository.searchByKeyword(keyword, pageable).map(IdeaDTO.Response::from);
    }

    @Transactional(readOnly = true)
    public List<IdeaDTO.Response> findTopIdeas(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ideaRepository.findTopIdeasByVoteScore(pageable)
                .stream()
                .map(IdeaDTO.Response::from)
                .collect(Collectors.toList());
    }

    public IdeaDTO.Response update(Long id, IdeaDTO.UpdateRequest request, String requestingUsername) {
        Idea idea = getIdeaOrThrow(id);
        assertOwnerOrAdmin(idea, requestingUsername);

        if (request.getTitle() != null) idea.setTitle(request.getTitle());
        if (request.getDescription() != null) idea.setDescription(request.getDescription());
        if (request.getStatus() != null) idea.setStatus(request.getStatus());

        Idea updated = ideaRepository.save(idea);
        log.info("Updated idea id={}", id);
        return IdeaDTO.Response.from(updated);
    }

    public void delete(Long id, String requestingUsername) {
        Idea idea = getIdeaOrThrow(id);
        assertOwnerOrAdmin(idea, requestingUsername);
        ideaRepository.delete(idea);
        log.info("Deleted idea id={}", id);
    }

    private Idea getIdeaOrThrow(Long id) {
        return ideaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Idea not found with id: " + id));
    }

    private void assertOwnerOrAdmin(Idea idea, String username) {
        User user = userService.findByUsername(username);
        boolean isOwner = idea.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == User.Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new SecurityException("You are not allowed to modify this idea.");
        }
    }
}