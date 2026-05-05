package edu.cit.chan.restoradar;

import edu.cit.chan.restoradar.feature.review.ReviewService;
import edu.cit.chan.restoradar.feature.review.ReviewEntity;
import edu.cit.chan.restoradar.feature.review.ReviewRepository;
import edu.cit.chan.restoradar.feature.restaurant.RestaurantEntity;
import edu.cit.chan.restoradar.feature.restaurant.RestaurantRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ReviewService reviewService;

    private UUID restaurantId;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
    }

    @Test
    void submitReview_WithValidData_ShouldSaveReview() {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(restaurantId);

        when(restaurantRepository.findById(restaurantId))
            .thenReturn(Optional.of(restaurant));
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ReviewEntity result = reviewService.submitReview(restaurantId, 4, "Great food!");

        assertNotNull(result);
        assertEquals(4, result.getrating());
        assertEquals("Great food!", result.getComment());
        verify(reviewRepository, times(1)).save(any());
    }

    @Test
    void submitReview_WithScore1_ShouldSaveReview() {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(restaurantId);

        when(restaurantRepository.findById(restaurantId))
            .thenReturn(Optional.of(restaurant));
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ReviewEntity result = reviewService.submitReview(restaurantId, 1, "Very bad.");

        assertNotNull(result);
        assertEquals(1, result.getrating());
    }

    @Test
    void submitReview_WithScore5_ShouldSaveReview() {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(restaurantId);

        when(restaurantRepository.findById(restaurantId))
            .thenReturn(Optional.of(restaurant));
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ReviewEntity result = reviewService.submitReview(restaurantId, 5, "Perfect!");

        assertNotNull(result);
        assertEquals(5, result.getrating());
    }

    @Test
    void submitReview_ShouldPublishEvent() {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(restaurantId);

        when(restaurantRepository.findById(restaurantId))
            .thenReturn(Optional.of(restaurant));
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        reviewService.submitReview(restaurantId, 3, "Decent place.");

        verify(eventPublisher, times(1)).publishEvent(any());
    }
}