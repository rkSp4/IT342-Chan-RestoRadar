import { mockRestaurants } from "../data/mockData";
import { RestaurantCard } from "../components/RestaurantCard";
import { Heart } from "lucide-react";
import { useRootContext } from "../Root";

export function FavoritesPage() {
  const { favorites, onToggleFavorite } = useRootContext();
  const favoriteRestaurants = mockRestaurants.filter((restaurant) =>
    favorites.includes(restaurant.id)
  );

  return (
    <div className="min-h-screen bg-gray-50 pb-20 md:pb-6">
      <div className="max-w-7xl mx-auto px-4 md:px-6 py-6">
        <div className="mb-6">
          <h1 className="text-3xl font-bold mb-2">Favorite Restaurants</h1>
          <p className="text-gray-600">
            {favoriteRestaurants.length} saved restaurant{favoriteRestaurants.length !== 1 ? 's' : ''}
          </p>
        </div>

        {favoriteRestaurants.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {favoriteRestaurants.map((restaurant) => (
              <RestaurantCard
                key={restaurant.id}
                restaurant={restaurant}
                isFavorite={true}
                onToggleFavorite={onToggleFavorite}
              />
            ))}
          </div>
        ) : (
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
          </div>
        )}
      </div>
    </div>
  );
}