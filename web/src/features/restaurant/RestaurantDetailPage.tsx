import { useParams, Link } from "react-router";
import { useState, useEffect } from "react";
import { StarRating } from "../review/StarRating";
import { ReviewCard } from "../review/ReviewCard";
import { MapPin, Phone, Clock, Heart, ArrowLeft, Share2, Globe } from "lucide-react";
import { Button } from "../../shared/components/ui/button";
import { ImageWithFallback } from "../../shared/components/figma/ImageWithFallback";
import { useRootContext } from "../../Root";
import { restaurantService, Restaurant } from "../../shared/services/restaurantService";
import { reviewService, Review } from "../../shared/services/services";

export function RestaurantDetailPage() {
  const { favorites, onToggleFavorite } = useRootContext();
  const { id } = useParams<{ id: string }>();

  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    const load = async () => {
      setLoading(true);
      setError(null);
      try {
        const [restaurantData, reviewData] = await Promise.all([
          restaurantService.getById(id),
          reviewService.getByRestaurant(id),
        ]);
        setRestaurant(restaurantData);
        setReviews(reviewData.reviews);
      } catch {
        setError("Failed to load restaurant details.");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [id]);

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50">
        <div className="h-64 md:h-96 bg-gray-200 animate-pulse" />
        <div className="max-w-4xl mx-auto px-4 md:px-6">
          <div className="bg-white rounded-lg shadow-md p-6 -mt-8 relative z-10 animate-pulse">
            <div className="h-8 bg-gray-200 rounded w-1/2 mb-4" />
            <div className="h-4 bg-gray-200 rounded w-1/4 mb-6" />
            <div className="h-4 bg-gray-200 rounded w-full mb-2" />
            <div className="h-4 bg-gray-200 rounded w-3/4" />
          </div>
        </div>
      </div>
    );
  }

  if (error || !restaurant) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold mb-2">{error ?? "Restaurant not found"}</h2>
          <Link to="/explore">
            <Button>Back to Explore</Button>
          </Link>
        </div>
      </div>
    );
  }

  const isFavorite = favorites.includes(restaurant.id);

  return (
    <div className="min-h-screen bg-gray-50 pb-20 md:pb-6">
      {/* Header Image */}
      <div className="relative h-64 md:h-96">
        <ImageWithFallback
          src={restaurant.photos ?? ""}
          alt={restaurant.name}
          className="w-full h-full object-cover"
        />
        <div className="absolute top-4 left-4">
          <Link to="/explore">
            <Button variant="secondary" size="icon" className="rounded-full">
              <ArrowLeft size={20} />
            </Button>
          </Link>
        </div>
        <div className="absolute top-4 right-4 flex gap-2">
          <Button
            variant="secondary"
            size="icon"
            className="rounded-full"
            onClick={() => onToggleFavorite(restaurant.id)}
          >
            <Heart
              size={20}
              className={isFavorite ? "text-red-500 fill-red-500" : ""}
            />
          </Button>
          <Button variant="secondary" size="icon" className="rounded-full">
            <Share2 size={20} />
          </Button>
        </div>
      </div>

      {/* Restaurant Info */}
      <div className="max-w-4xl mx-auto px-4 md:px-6">
        <div className="bg-white rounded-lg shadow-md p-6 -mt-8 relative z-10 mb-6">
          <div className="flex items-start justify-between mb-4">
            <div>
              <h1 className="text-3xl font-bold mb-2">{restaurant.name}</h1>
              {restaurant.cuisineType && (
                <p className="text-gray-600">{restaurant.cuisineType}</p>
              )}
            </div>
            {restaurant.priceRange && (
              <div className="text-lg font-semibold">{restaurant.priceRange}</div>
            )}
          </div>

          <div className="flex items-center gap-3 mb-4">
            <StarRating rating={restaurant.averageRating} size={20} showNumber />
            <span className="text-gray-500">({restaurant.reviewCount} reviews)</span>
          </div>

          <div className="space-y-3 mb-6">
            <div className="flex items-start gap-3 text-gray-700">
              <MapPin size={20} className="mt-0.5 flex-shrink-0" />
              <p>{restaurant.address}</p>
            </div>
            {restaurant.operatingHours && (
              <div className="flex items-center gap-3 text-gray-700">
                <Clock size={20} className="flex-shrink-0" />
                <span>{restaurant.operatingHours}</span>
              </div>
            )}
            {restaurant.contactNumber && (
              <div className="flex items-center gap-3 text-gray-700">
                <Phone size={20} className="flex-shrink-0" />
                <a href={`tel:${restaurant.contactNumber}`} className="hover:text-blue-600">
                  {restaurant.contactNumber}
                </a>
              </div>
            )}
            {restaurant.website && (
              <div className="flex items-center gap-3 text-gray-700">
                <Globe size={20} className="flex-shrink-0" />
                <a
                  href={restaurant.website}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="hover:text-blue-600 truncate"
                >
                  {restaurant.website}
                </a>
              </div>
            )}
          </div>

          <div className="flex gap-3">
            <Link to={`/restaurant/${restaurant.id}/review`} className="flex-1">
              <Button className="w-full">Write a Review</Button>
            </Link>
            <a
              href={`https://www.google.com/maps/search/?api=1&query=${restaurant.latitude},${restaurant.longitude}`}
              target="_blank"
              rel="noopener noreferrer"
              className="flex-1"
            >
              <Button variant="outline" className="w-full">Get Directions</Button>
            </a>
          </div>
        </div>

        {/* Reviews Section */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-2xl font-bold mb-6">Reviews ({reviews.length})</h2>
          {reviews.length > 0 ? (
            <div className="space-y-4">
              {reviews.map((review) => (
                <ReviewCard key={review.id} review={review} />
              ))}
            </div>
          ) : (
            <div className="text-center py-12 text-gray-500">
              <p>No reviews yet. Be the first to review!</p>
              <Link to={`/restaurant/${restaurant.id}/review`}>
                <Button className="mt-4">Write a Review</Button>
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}