package edu.cit.chan.restoradar.feature.review;

import org.springframework.context.ApplicationEvent;

/**
 * Event acting as the subject core data for the Observer pattern.
 */
public class ReviewCreatedEvent extends ApplicationEvent {
    
    private final ReviewEntity review;

    public ReviewCreatedEvent(Object source, ReviewEntity review) {
        super(source);
        this.review = review;
    }

    public ReviewEntity getReview() {
        return review;
    }
}
