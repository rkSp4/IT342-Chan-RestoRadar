package edu.cit.chan.restoradar.observer;

import edu.cit.chan.restoradar.entity.RestaurantEntity;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer that pushes notifications (or emails) to
 * the restaurant owner whenever a ReviewCreatedEvent occurs.
 */
@Component
public class OwnerNotifyingListener {

    @EventListener
    public void notifyOwner(ReviewCreatedEvent event) {
        RestaurantEntity restaurant = event.getReview().getRestaurant();

        System.out.println("OBSERVER TRIGGERED: Sending email/push notification to Owner -> " 
                           + " Your restaurant " + restaurant.getName() 
                           + " received a new " + event.getReview().getScore() + "-star review!");
        
        // This is where external Push Notification Service logic would run.
    }
}
