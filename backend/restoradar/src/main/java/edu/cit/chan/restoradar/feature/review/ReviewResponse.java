package edu.cit.chan.restoradar.feature.review;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewResponse {
    private UUID id;
    private int score;
    private String comment;
    private LocalDateTime createdAt;
    
    // In a full app, you might include the Author's name or UserDTO as well.
    public ReviewResponse() {}
    
    public ReviewResponse(UUID id, int score, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.score = score;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
