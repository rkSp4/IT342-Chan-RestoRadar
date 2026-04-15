package edu.cit.chan.restoradar.service;

import edu.cit.chan.restoradar.entity.RestaurantEntity;
import edu.cit.chan.restoradar.entity.ReviewEntity;
import edu.cit.chan.restoradar.repository.RestaurantRepository;
import edu.cit.chan.restoradar.repository.ReviewRepository;
import edu.cit.chan.restoradar.observer.ReviewCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReviewService(ReviewRepository reviewRepository,
                         RestaurantRepository restaurantRepository,
                         ApplicationEventPublisher eventPublisher) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Submit a new review and trigger the Observer pattern events immediately!
     */
    @Transactional
    public ReviewEntity submitReview(UUID restaurantId, int score, String comment) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseGet(() -> {
                    RestaurantEntity dummy = new RestaurantEntity();
                    dummy.setName("Mocked Restaurant for Reviews");
                    dummy.setAddress("Mock");
                    dummy.setLatitude(0.0);
                    dummy.setLongitude(0.0);
                    return restaurantRepository.save(dummy);
                });

        ReviewEntity review = new ReviewEntity(restaurant, score, comment);
        review = reviewRepository.save(review);

        // Subject notifies all interested Observers
        eventPublisher.publishEvent(new ReviewCreatedEvent(this, review));

        return review;
    }
}
