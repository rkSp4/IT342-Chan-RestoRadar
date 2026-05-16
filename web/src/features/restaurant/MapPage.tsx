import { useState } from "react";
import { mockRestaurants } from "../restaurant/mockData";
import { MapPin, Navigation } from "lucide-react";
import { Button } from "../../shared/components/ui/button";
import { StarRating } from "../../features/review/StarRating";
import { Link } from "react-router";
import { useRootContext } from "../../Root";

export function MapPage() {
  const { favorites } = useRootContext();
  const [selectedRestaurant, setSelectedRestaurant] = useState<string | null>(null);
  const selected = mockRestaurants.find((r) => r.id === selectedRestaurant);

  return (
    <div className="h-screen bg-gray-50 pb-16 md:pb-0 flex flex-col">
      <div className="flex-1 relative overflow-hidden">
        {/* Mock Map Background */}
        <div className="w-full h-full bg-gradient-to-br from-blue-50 to-green-50 relative">
          {/* Mock map grid */}
          <div className="absolute inset-0 opacity-10">
            <div className="grid grid-cols-8 grid-rows-8 h-full w-full">
              {Array.from({ length: 64 }).map((_, i) => (
                <div key={i} className="border border-gray-400" />
              ))}
            </div>
          </div>

          {/* Location markers */}
          {mockRestaurants.map((restaurant, index) => {
            const isFavorite = favorites.includes(restaurant.id);
            const isSelected = selectedRestaurant === restaurant.id;
            
            // Position restaurants in a visually pleasing way
            const positions = [
              { top: '20%', left: '30%' },
              { top: '35%', left: '60%' },
              { top: '50%', left: '25%' },
              { top: '25%', left: '70%' },
              { top: '60%', left: '45%' },
              { top: '70%', left: '65%' },
              { top: '40%', left: '80%' },
              { top: '75%', left: '30%' }
            ];

            return (
              <button
                key={restaurant.id}
                onClick={() => setSelectedRestaurant(restaurant.id)}
                className="absolute transform -translate-x-1/2 -translate-y-1/2 transition-all"
                style={positions[index]}
              >
                <div className={`relative ${isSelected ? 'scale-125' : ''}`}>
                  <MapPin
                    size={isSelected ? 40 : 32}
                    className={`${
                      isFavorite
                        ? 'text-red-500 fill-red-500'
                        : 'text-blue-600 fill-blue-600'
                    } drop-shadow-lg`}
                  />
                  {isSelected && (
                    <div className="absolute -top-2 -right-2 bg-blue-600 text-white rounded-full w-6 h-6 flex items-center justify-center text-xs font-bold">
                      ★
                    </div>
                  )}
                </div>
              </button>
            );
          })}

          {/* Current location indicator */}
          <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
            <div className="relative">
              <div className="absolute inset-0 bg-blue-400 rounded-full animate-ping opacity-75" />
              <div className="relative bg-blue-600 rounded-full p-2">
                <Navigation size={20} className="text-white" />
              </div>
            </div>
          </div>
        </div>

        {/* Info Card */}
        {selected && (
          <div className="absolute bottom-4 left-4 right-4 md:bottom-6 md:left-6 md:right-auto md:w-96">
            <div className="bg-white rounded-lg shadow-xl p-4">
              <div className="flex items-start gap-4">
                <img
                  src={selected.image}
                  alt={selected.name}
                  className="w-20 h-20 rounded-lg object-cover flex-shrink-0"
                />
                <div className="flex-1 min-w-0">
                  <h3 className="font-bold text-lg mb-1 truncate">{selected.name}</h3>
                  <p className="text-sm text-gray-600 mb-2">{selected.cuisine} • {selected.priceRange}</p>
                  <div className="flex items-center gap-2 mb-2">
                    <StarRating rating={selected.rating} size={14} />
                    <span className="text-xs text-gray-500">({selected.reviewCount})</span>
                  </div>
                  <p className="text-sm text-gray-600 mb-3">
                    <MapPin size={12} className="inline mr-1" />
                    {selected.distance} mi away
                  </p>
                  <div className="flex gap-2">
                    <Link to={`/restaurant/${selected.id}`} className="flex-1">
                      <Button size="sm" className="w-full">
                        View Details
                      </Button>
                    </Link>
                    <Button size="sm" variant="outline">
                      Directions
                    </Button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Map Controls */}
        <div className="absolute top-4 right-4 flex flex-col gap-2">
          <Button size="icon" variant="secondary" className="bg-white">
            +
          </Button>
          <Button size="icon" variant="secondary" className="bg-white">
            -
          </Button>
        </div>
      </div>

      {/* Header */}
      <div className="absolute top-0 left-0 right-0 bg-white/95 backdrop-blur-sm border-b border-gray-200 p-4 md:hidden">
        <h1 className="text-xl font-bold">Map View</h1>
        <p className="text-sm text-gray-600">Tap markers to see restaurant details</p>
      </div>
    </div>
  );
}