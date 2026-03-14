package com.idea.voting.repository;

import com.idea.voting.model.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {

    Page<Idea> findAll(Pageable pageable);

    Page<Idea> findByStatus(Idea.Status status, Pageable pageable);

    Page<Idea> findByAuthorId(Long authorId, Pageable pageable);

    @Query("SELECT i FROM Idea i WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Idea> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT i FROM Idea i LEFT JOIN i.votes v " +
           "GROUP BY i ORDER BY SUM(CASE WHEN v.voteType = 'UPVOTE' THEN 1 " +
           "WHEN v.voteType = 'DOWNVOTE' THEN -1 ELSE 0 END) DESC")
    List<Idea> findTopIdeasByVoteScore(Pageable pageable);
}
