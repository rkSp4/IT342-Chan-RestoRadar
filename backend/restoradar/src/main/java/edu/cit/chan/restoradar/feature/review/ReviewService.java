package edu.cit.chan.restoradar.feature.review;

import edu.cit.chan.restoradar.feature.restaurant.RestaurantEntity;
import edu.cit.chan.restoradar.feature.restaurant.RestaurantRepository;

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
    public ReviewEntity submitReview(UUID restaurantId, int rating, String comment) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseGet(() -> {
                    RestaurantEntity dummy = new RestaurantEntity();
                    dummy.setName("Mocked Restaurant for Reviews");
                    dummy.setAddress("Mock");
                    dummy.setLatitude(0.0);
                    dummy.setLongitude(0.0);
                    return restaurantRepository.save(dummy);
                });

        ReviewEntity review = new ReviewEntity(restaurant, rating, comment);
        review = reviewRepository.save(review);

        // Subject notifies all interested Observers
        eventPublisher.publishEvent(new ReviewCreatedEvent(this, review));

        return review;
    }
}
