import { useState, useMemo } from "react";
import { mockRestaurants } from "../data/mockData";
import { RestaurantCard } from "../components/RestaurantCard";
import { SearchBar } from "../components/SearchBar";
import { FilterBar } from "../components/FilterBar";
import { SlidersHorizontal } from "lucide-react";
import { Button } from "../components/ui/button";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "../components/ui/sheet";
import { useRootContext } from "../Root";

export function ExplorePage() {
  const { favorites, onToggleFavorite } = useRootContext();
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedCuisine, setSelectedCuisine] = useState("All");
  const [selectedPriceRange, setSelectedPriceRange] = useState("All");

  const filteredRestaurants = useMemo(() => {
    return mockRestaurants.filter((restaurant) => {
      const matchesSearch =
        restaurant.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        restaurant.cuisine.toLowerCase().includes(searchQuery.toLowerCase()) ||
        restaurant.tags.some(tag => tag.toLowerCase().includes(searchQuery.toLowerCase()));

      const matchesCuisine =
        selectedCuisine === "All" || restaurant.cuisine === selectedCuisine;

      const matchesPrice =
        selectedPriceRange === "All" || restaurant.priceRange === selectedPriceRange;

      return matchesSearch && matchesCuisine && matchesPrice;
    });
  }, [searchQuery, selectedCuisine, selectedPriceRange]);

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
                placeholder="Search restaurants, cuisines, or tags..."
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

        {/* Results Count */}
        <div className="mb-4">
          <p className="text-gray-600">
            {filteredRestaurants.length} restaurant{filteredRestaurants.length !== 1 ? 's' : ''} found
          </p>
        </div>

        {/* Restaurant Grid */}
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
            <p className="text-gray-500 text-lg">No restaurants found matching your criteria</p>
            <Button
              variant="link"
              onClick={() => {
                setSearchQuery("");
                setSelectedCuisine("All");
                setSelectedPriceRange("All");
              }}
              className="mt-4"
            >
              Clear all filters
            </Button>
          </div>
        )}
      </div>
    </div>
  );
}