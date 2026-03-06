import { useParams, Link } from "react-router";
import { mockRestaurants, mockReviews } from "../data/mockData";
import { StarRating } from "../components/StarRating";
import { ReviewCard } from "../components/ReviewCard";
import { MapPin, Phone, Clock, Heart, ArrowLeft, Share2 } from "lucide-react";
import { Button } from "../components/ui/button";
import { ImageWithFallback } from "../components/figma/ImageWithFallback";
import { useRootContext } from "../Root";

export function RestaurantDetailPage() {
  const { favorites, onToggleFavorite } = useRootContext();
  const { id } = useParams();
  const restaurant = mockRestaurants.find((r) => r.id === id);
  const reviews = mockReviews.filter((r) => r.restaurantId === id);

  if (!restaurant) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold mb-2">Restaurant not found</h2>
          <Link to="/">
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
          src={restaurant.image}
          alt={restaurant.name}
          className="w-full h-full object-cover"
        />
        <div className="absolute top-4 left-4">
          <Link to="/">
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
              <p className="text-gray-600">{restaurant.cuisine}</p>
            </div>
            <div className="text-lg font-semibold">{restaurant.priceRange}</div>
          </div>

          <div className="flex items-center gap-3 mb-4">
            <StarRating rating={restaurant.rating} size={20} showNumber />
            <span className="text-gray-500">({restaurant.reviewCount} reviews)</span>
          </div>

          <div className="space-y-3 mb-6">
            <div className="flex items-start gap-3 text-gray-700">
              <MapPin size={20} className="mt-0.5 flex-shrink-0" />
              <div>
                <p>{restaurant.address}</p>
                <p className="text-sm text-gray-500">{restaurant.distance} miles away</p>
              </div>
            </div>
            <div className="flex items-center gap-3 text-gray-700">
              <Clock size={20} className="flex-shrink-0" />
              <span>{restaurant.hours}</span>
            </div>
            <div className="flex items-center gap-3 text-gray-700">
              <Phone size={20} className="flex-shrink-0" />
              <a href={`tel:${restaurant.phone}`} className="hover:text-blue-600">
                {restaurant.phone}
              </a>
            </div>
          </div>

          <div className="mb-6">
            <h2 className="font-semibold mb-2">About</h2>
            <p className="text-gray-700">{restaurant.description}</p>
          </div>

          <div className="flex flex-wrap gap-2 mb-6">
            {restaurant.tags.map((tag, index) => (
              <span
                key={index}
                className="inline-block px-3 py-1 bg-blue-50 text-blue-700 rounded-full text-sm"
              >
                {tag}
              </span>
            ))}
          </div>

          <div className="flex gap-3">
            <Link to={`/restaurant/${restaurant.id}/review`} className="flex-1">
              <Button className="w-full">Write a Review</Button>
            </Link>
            <Button variant="outline" className="flex-1">
              Get Directions
            </Button>
          </div>
        </div>

        {/* Reviews Section */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-2xl font-bold mb-6">
            Reviews ({reviews.length})
          </h2>
          {reviews.length > 0 ? (
            <div>
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