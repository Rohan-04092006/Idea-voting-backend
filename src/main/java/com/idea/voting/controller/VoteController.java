package com.idea.voting.controller;

import com.idea.voting.model.Vote;
import com.idea.voting.service.VoteService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {
	@Autowired
    private  VoteService voteService;

    // POST /api/votes/ideas/{ideaId}/upvote
	@PostMapping("/ideas/{ideaId}/upvote")
	public ResponseEntity<Map<String, Object>> upvote(
	        @PathVariable Long ideaId,
	        @AuthenticationPrincipal UserDetails principal) {

	    if (principal == null) {
	        return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
	    }

	    Map<String, Object> result = voteService.vote(
	            ideaId,
	            Vote.VoteType.UPVOTE,
	            principal.getUsername()
	    );

	    return ResponseEntity.ok(result);
	}

    // POST /api/votes/ideas/{ideaId}/downvote
	@PostMapping("/ideas/{ideaId}/downvote")
	public ResponseEntity<Map<String, Object>> downvote(
	        @PathVariable Long ideaId,
	        @AuthenticationPrincipal UserDetails principal) {

	    if (principal == null) {
	        return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
	    }

	    Map<String, Object> result = voteService.vote(
	            ideaId,
	            Vote.VoteType.DOWNVOTE,
	            principal.getUsername()
	    );

	    return ResponseEntity.ok(result);
	}

    // GET /api/votes/ideas/{ideaId}/counts  (public)
    @GetMapping("/ideas/{ideaId}/counts")
    public ResponseEntity<Map<String, Long>> getCounts(@PathVariable Long ideaId) {
        return ResponseEntity.ok(voteService.getVoteCounts(ideaId));
    }

    // GET /api/votes/ideas/{ideaId}/my-vote
    @GetMapping("/ideas/{ideaId}/my-vote")
    public ResponseEntity<Map<String, String>> getMyVote(
            @PathVariable Long ideaId,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.ok(Map.of("vote", "NONE"));
        }

        Optional<Vote.VoteType> myVote =
                voteService.getUserVote(ideaId, principal.getUsername());

        String voteValue = myVote.map(Enum::name).orElse("NONE");

        return ResponseEntity.ok(Map.of("vote", voteValue));
    }
}
