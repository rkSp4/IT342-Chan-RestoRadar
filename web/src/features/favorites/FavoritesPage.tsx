import { useState, useEffect } from "react";
import { RestaurantCard } from "../restaurant/RestaurantCard";
import { Heart } from "lucide-react";
import { Link } from "react-router";
import { Button } from "../../shared/components/ui/button";
import { useRootContext } from "../../Root";
import { useAuth } from "../auth/AuthContext";
import { favoriteService } from "../../shared/services/services";
import type { Favorite } from "../../shared/services/services";
import type { Restaurant } from "../../shared/services/restaurantService";

export function FavoritesPage() {
  const { favorites, onToggleFavorite } = useRootContext();
  const { user, isAuthenticated } = useAuth();
  const [favoriteRestaurants, setFavoriteRestaurants] = useState<Restaurant[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isAuthenticated || !user) { setLoading(false); return; }
    favoriteService.getByUser(user.id)
      .then((res) => {
        const mapped: Restaurant[] = res.favorites.map((f: Favorite) => ({
          id: f.restaurantId,
          name: f.restaurantName,
          address: "",
          cuisineType: "",
          priceRange: f.restaurantPriceRange,
          averageRating: f.restaurantRating,
          reviewCount: 0,
          latitude: 0,
          longitude: 0,
          photos: f.restaurantImageUrl,
        }));
        setFavoriteRestaurants(mapped);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [isAuthenticated, user]);

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
        <div className="bg-white rounded-lg shadow-md p-8 max-w-md w-full text-center">
          <Heart size={48} className="mx-auto mb-4 text-gray-300" />
          <h2 className="text-2xl font-bold mb-2">Sign In Required</h2>
          <p className="text-gray-600 mb-6">Sign in to view your saved restaurants.</p>
          <Link to="/login"><Button className="w-full">Sign In</Button></Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20 md:pb-6">
      <div className="max-w-7xl mx-auto px-4 md:px-6 py-6">
        <div className="mb-6">
          <h1 className="text-3xl font-bold mb-2">Favorite Restaurants</h1>
          <p className="text-gray-600">
            {favoriteRestaurants.length} saved restaurant{favoriteRestaurants.length !== 1 ? "s" : ""}
          </p>
        </div>

        {loading && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(3)].map((_, i) => (
              <div key={i} className="bg-white rounded-xl h-64 animate-pulse" />
            ))}
          </div>
        )}

        {!loading && favoriteRestaurants.length > 0 && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {favoriteRestaurants.map((restaurant) => (
              <RestaurantCard
                key={restaurant.id}
                restaurant={restaurant}
                isFavorite={favorites.includes(restaurant.id)}
                onToggleFavorite={onToggleFavorite}
              />
            ))}
          </div>
        )}

        {!loading && favoriteRestaurants.length === 0 && (
          <div className="bg-white rounded-lg shadow-md p-12 text-center">
            <div className="flex justify-center mb-4">
              <div className="bg-gray-100 p-6 rounded-full">
                <Heart size={48} className="text-gray-400" />
              </div>
            </div>
            <h2 className="text-2xl font-bold mb-2">No Favorites Yet</h2>
            <p className="text-gray-600 mb-6">
              Start exploring and save your favorite restaurants to see them here!
            </p>
            <Link to="/explore"><Button>Explore Restaurants</Button></Link>
          </div>
        )}
      </div>
    </div>
  );
}