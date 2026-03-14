package com.idea.voting.repository;

import com.idea.voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserIdAndIdeaId(Long userId, Long ideaId);

    boolean existsByUserIdAndIdeaId(Long userId, Long ideaId);

    long countByIdeaIdAndVoteType(Long ideaId, Vote.VoteType voteType);

    void deleteByUserIdAndIdeaId(Long userId, Long ideaId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.idea.id = :ideaId AND v.voteType = 'UPVOTE'")
    long countUpvotes(@Param("ideaId") Long ideaId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.idea.id = :ideaId AND v.voteType = 'DOWNVOTE'")
    long countDownvotes(@Param("ideaId") Long ideaId);
}
