package edu.cit.chan.restoradar;

import edu.cit.chan.restoradar.feature.restaurant.RestaurantEntity;
import edu.cit.chan.restoradar.feature.restaurant.RestaurantRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    void findById_WithValidId_ShouldReturnRestaurant() {
        UUID id = UUID.randomUUID();
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(id);
        restaurant.setName("Test Restaurant");

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        Optional<RestaurantEntity> result = restaurantRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Test Restaurant", result.get().getName());
    }

    @Test
    void findById_WithInvalidId_ShouldReturnEmpty() {
        UUID id = UUID.randomUUID();
        when(restaurantRepository.findById(id)).thenReturn(Optional.empty());

        Optional<RestaurantEntity> result = restaurantRepository.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnList() {
        RestaurantEntity r1 = new RestaurantEntity();
        r1.setName("Restaurant A");
        RestaurantEntity r2 = new RestaurantEntity();
        r2.setName("Restaurant B");

        when(restaurantRepository.findAll()).thenReturn(List.of(r1, r2));

        List<RestaurantEntity> results = restaurantRepository.findAll();

        assertEquals(2, results.size());
    }
}