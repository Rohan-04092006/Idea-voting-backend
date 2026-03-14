package com.idea.voting.dto;

import com.idea.voting.model.Idea;
import java.time.LocalDateTime;

public class IdeaDTO {

    public static class CreateRequest {

        private String title;
        private String description;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class UpdateRequest {

        private String title;
        private String description;
        private Idea.Status status;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Idea.Status getStatus() {
            return status;
        }

        public void setStatus(Idea.Status status) {
            this.status = status;
        }
    }

    public static class Response {

        private Long id;
        private String title;
        private String description;
        private Idea.Status status;
        private String authorUsername;
        private long upvotes;
        private long downvotes;
        private long totalVotes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response from(Idea idea) {
            Response r = new Response();
            r.id = idea.getId();
            r.title = idea.getTitle();
            r.description = idea.getDescription();
            r.status = idea.getStatus();
            r.authorUsername = idea.getAuthor().getUsername();
            r.upvotes = idea.getUpvoteCount();
            r.downvotes = idea.getDownvoteCount();
            r.totalVotes = idea.getUpvoteCount() - idea.getDownvoteCount();
            r.createdAt = idea.getCreatedAt();
            r.updatedAt = idea.getUpdatedAt();
            return r;
        }

        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public Idea.Status getStatus() { return status; }
        public String getAuthorUsername() { return authorUsername; }
        public long getUpvotes() { return upvotes; }
        public long getDownvotes() { return downvotes; }
        public long getTotalVotes() { return totalVotes; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
    }

    public static class UserSummary {

        private Long id;
        private String username;
        private String email;

        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }

        public void setId(Long id) { this.id = id; }
        public void setUsername(String username) { this.username = username; }
        public void setEmail(String email) { this.email = email; }
    }
}