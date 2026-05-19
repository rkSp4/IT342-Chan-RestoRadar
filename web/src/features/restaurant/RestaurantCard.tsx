import { Heart, MapPin } from "lucide-react";
import { Restaurant } from "../../shared/services/restaurantService";
import { StarRating } from "../../features/review/StarRating";
import { Link } from "react-router";
import { ImageWithFallback } from "../../shared/components/figma/ImageWithFallback";

interface RestaurantCardProps {
  restaurant: Restaurant;
  isFavorite?: boolean;
  onToggleFavorite?: (id: string) => void;
}

export function RestaurantCard({
  restaurant,
  isFavorite = false,
  onToggleFavorite,
}: RestaurantCardProps) {
  return (
    <Link to={`/restaurant/${restaurant.id}`} className="block">
      <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
        <div className="relative h-48">
          <ImageWithFallback
            src={restaurant.photos ?? ""}
            alt={restaurant.name}
            className="w-full h-full object-cover"
          />
          <button
            onClick={(e) => {
              e.preventDefault();
              onToggleFavorite?.(restaurant.id);
            }}
            className="absolute top-3 right-3 bg-white p-2 rounded-full shadow-md hover:bg-gray-50 transition-colors"
          >
            <Heart
              size={20}
              className={isFavorite ? "text-red-500 fill-red-500" : "text-gray-600"}
            />
          </button>
          {restaurant.priceRange && (
            <div className="absolute bottom-3 left-3 bg-white px-2 py-1 rounded-md text-sm">
              {restaurant.priceRange}
            </div>
          )}
        </div>

        <div className="p-4">
          <div className="flex items-start justify-between mb-2">
            <div className="flex-1">
              <h3 className="font-semibold text-lg mb-1">{restaurant.name}</h3>
              {restaurant.cuisineType && (
                <p className="text-sm text-gray-600">{restaurant.cuisineType}</p>
              )}
            </div>
          </div>

          <div className="flex items-center gap-2 mb-3">
            <StarRating rating={restaurant.averageRating} size={16} showNumber />
            <span className="text-sm text-gray-500">({restaurant.reviewCount})</span>
          </div>

          <div className="flex items-center gap-4 text-sm text-gray-600">
            <div className="flex items-center gap-1">
              <MapPin size={14} />
              <span className="truncate max-w-[180px]">{restaurant.address}</span>
            </div>
            {restaurant.distance !== undefined && (
              <span>{restaurant.distance} km</span>
            )}
          </div>
        </div>
      </div>
    </Link>
  );
}