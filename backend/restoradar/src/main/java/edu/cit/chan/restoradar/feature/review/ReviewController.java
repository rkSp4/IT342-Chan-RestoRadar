package edu.cit.chan.restoradar.feature.review;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/restaurants/{id}/reviews")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> addReview(@PathVariable("id") String id, @RequestBody ReviewRequest request) {
        try {
            // Check if mock IDs are being passed
            UUID restaurantId;
            try {
                restaurantId = UUID.fromString(id);
            } catch (IllegalArgumentException ex) {
                // If it's a frontend mock ID (1, 2, 3), fake a success locally
                // because the real database needs full UUIDs for restaurants
                if (id != null && id.length() < 10) {
                    // We map the mock "1" or "2" into a static UUID that we generate/seed
                    restaurantId = UUID.nameUUIDFromBytes(id.getBytes());
                } else {
                    throw ex;
                }
            }

            ReviewEntity review = reviewService.submitReview(restaurantId, request.getrating(), request.getComment());
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Review submitted successfully",
                "reviewId", review.getId().toString()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid restaurant ID"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "Error submitting review: " + e.getMessage()));
        }
    }
}
