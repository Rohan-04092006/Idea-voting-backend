package com.idea.voting.service;

import com.idea.voting.model.*;
import com.idea.voting.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service

@Transactional
public class VoteService {

	private final VoteRepository voteRepository;
	private final IdeaRepository ideaRepository;
	private final UserService userService;

	public VoteService(VoteRepository voteRepository, IdeaRepository ideaRepository, UserService userService) {

		this.voteRepository = voteRepository;
		this.ideaRepository = ideaRepository;
		this.userService = userService;
	}
	private static final Logger log = LoggerFactory.getLogger(VoteService.class);

	public Map<String, Object> vote(Long ideaId, Vote.VoteType voteType, String username) {
		User user = userService.findByUsername(username);
		Idea idea = ideaRepository.findById(ideaId)
				.orElseThrow(() -> new IllegalArgumentException("Idea not found: " + ideaId));

		if (idea.getStatus() != Idea.Status.OPEN) {
			throw new IllegalStateException("Voting is only allowed on OPEN ideas.");
		}

		Optional<Vote> existingVote = voteRepository.findByUserIdAndIdeaId(user.getId(), ideaId);
		String action;

		if (existingVote.isPresent()) {
			Vote vote = existingVote.get();
			if (vote.getVoteType() == voteType) {
				voteRepository.delete(vote);
				action = "removed";
				log.info("User {} removed {} on idea {}", username, voteType, ideaId);
			} else {
				vote.setVoteType(voteType);
				voteRepository.save(vote);
				action = "updated";
				log.info("User {} switched vote to {} on idea {}", username, voteType, ideaId);
			}
		} else {
			Vote newVote = new Vote();
			newVote.setUser(user);
			newVote.setIdea(idea);
			newVote.setVoteType(voteType);
			voteRepository.save(newVote);
			action = "created";
			log.info("User {} cast {} on idea {}", username, voteType, ideaId);
		}

		long upvotes = voteRepository.countUpvotes(ideaId);
		long downvotes = voteRepository.countDownvotes(ideaId);

		return Map.of("action", action, "ideaId", ideaId, "upvotes", upvotes, "downvotes", downvotes, "score",
				upvotes - downvotes);
	}

	@Transactional(readOnly = true)
	public Map<String, Long> getVoteCounts(Long ideaId) {
		if (!ideaRepository.existsById(ideaId)) {
			throw new IllegalArgumentException("Idea not found: " + ideaId);
		}
		long upvotes = voteRepository.countUpvotes(ideaId);
		long downvotes = voteRepository.countDownvotes(ideaId);
		return Map.of("upvotes", upvotes, "downvotes", downvotes, "score", upvotes - downvotes);
	}

	@Transactional(readOnly = true)
	public Optional<Vote.VoteType> getUserVote(Long ideaId, String username) {
		User user = userService.findByUsername(username);
		return voteRepository.findByUserIdAndIdeaId(user.getId(), ideaId).map(Vote::getVoteType);
	}
}