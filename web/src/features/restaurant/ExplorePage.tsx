import { useState, useEffect, useCallback } from "react";
import { RestaurantCard } from "../../features/restaurant/RestaurantCard";
import { SearchBar } from "../../features/restaurant/SearchBar";
import { FilterBar } from "../../features/restaurant/FilterBar";
import { SlidersHorizontal } from "lucide-react";
import { Button } from "../../shared/components/ui/button";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "../../shared/components/ui/sheet";
import { useRootContext } from "../../Root";
import { restaurantService, Restaurant } from "../../shared/services/restaurantService";

export function ExplorePage() {
  const rootContext = useRootContext();
  if (!rootContext) return null;
  const { favorites, onToggleFavorite } = rootContext;

  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedCuisine, setSelectedCuisine] = useState("All");
  const [selectedPriceRange, setSelectedPriceRange] = useState("All");

  const fetchRestaurants = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      let result;
      if (searchQuery.trim()) {
        result = await restaurantService.search(searchQuery);
      } else {
        result = await restaurantService.getAll();
      }
      setRestaurants(result.restaurants);
    } catch {
      setError("Failed to load restaurants. Please try again.");
    } finally {
      setLoading(false);
    }
  }, [searchQuery]);

  // Debounce search — only fire after user stops typing for 400ms
  useEffect(() => {
    const timer = setTimeout(fetchRestaurants, 400);
    return () => clearTimeout(timer);
  }, [fetchRestaurants]);

  // Filter locally by cuisine and price range (fast, no extra API call)
  const filteredRestaurants = restaurants.filter((r) => {
    const matchesCuisine =
      selectedCuisine === "All" || r.cuisineType === selectedCuisine;
    const matchesPrice =
      selectedPriceRange === "All" || r.priceRange === selectedPriceRange;
    return matchesCuisine && matchesPrice;
  });

  const clearFilters = () => {
    setSearchQuery("");
    setSelectedCuisine("All");
    setSelectedPriceRange("All");
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Mobile Header */}
      <div className="md:hidden sticky top-0 bg-white border-b border-gray-200 p-4 z-40">
        <h1 className="text-2xl font-bold mb-3">Explore</h1>
        <SearchBar
          value={searchQuery}
          onChange={setSearchQuery}
          placeholder="Search restaurants, cuisines..."
        />
      </div>

      <div className="max-w-7xl mx-auto p-4 md:p-6 pb-20 md:pb-6">
        {/* Desktop Header */}
        <div className="hidden md:block mb-6">
          <h1 className="text-3xl font-bold mb-4">Discover Restaurants</h1>
          <div className="flex gap-4">
            <div className="flex-1">
              <SearchBar
                value={searchQuery}
                onChange={setSearchQuery}
                placeholder="Search restaurants, cuisines..."
              />
            </div>
          </div>
        </div>

        {/* Filters - Desktop */}
        <div className="hidden md:block mb-6">
          <FilterBar
            selectedCuisine={selectedCuisine}
            selectedPriceRange={selectedPriceRange}
            onCuisineChange={setSelectedCuisine}
            onPriceRangeChange={setSelectedPriceRange}
          />
        </div>

        {/* Filters - Mobile */}
        <div className="md:hidden mb-4">
          <Sheet>
            <SheetTrigger asChild>
              <Button variant="outline" className="w-full">
                <SlidersHorizontal size={16} className="mr-2" />
                Filters
              </Button>
            </SheetTrigger>
            <SheetContent side="bottom" className="h-[80vh]">
              <SheetHeader>
                <SheetTitle>Filters</SheetTitle>
              </SheetHeader>
              <div className="mt-6">
                <FilterBar
                  selectedCuisine={selectedCuisine}
                  selectedPriceRange={selectedPriceRange}
                  onCuisineChange={setSelectedCuisine}
                  onPriceRangeChange={setSelectedPriceRange}
                />
              </div>
            </SheetContent>
          </Sheet>
        </div>

        {/* Loading */}
        {loading && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(6)].map((_, i) => (
              <div key={i} className="bg-white rounded-xl h-64 animate-pulse" />
            ))}
          </div>
        )}

        {/* Error */}
        {error && !loading && (
          <div className="text-center py-12">
            <p className="text-red-500 text-lg">{error}</p>
            <Button variant="link" onClick={fetchRestaurants} className="mt-4">
              Try again
            </Button>
          </div>
        )}

        {/* Results */}
        {!loading && !error && (
          <>
            <div className="mb-4">
              <p className="text-gray-600">
                {filteredRestaurants.length} restaurant{filteredRestaurants.length !== 1 ? "s" : ""} found
              </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredRestaurants.map((restaurant) => (
                <RestaurantCard
                  key={restaurant.id}
                  restaurant={restaurant}
                  isFavorite={favorites.includes(restaurant.id)}
                  onToggleFavorite={onToggleFavorite}
                />
              ))}
            </div>

            {filteredRestaurants.length === 0 && (
              <div className="text-center py-12">
                <p className="text-gray-500 text-lg">
                  No restaurants found matching your criteria
                </p>
                <Button variant="link" onClick={clearFilters} className="mt-4">
                  Clear all filters
                </Button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}