import { cuisineCategories, priceRanges } from "../restaurant/mockData";
import { Button } from "../../shared/components/ui/button";

interface FilterBarProps {
  selectedCuisine: string;
  selectedPriceRange: string;
  onCuisineChange: (cuisine: string) => void;
  onPriceRangeChange: (priceRange: string) => void;
}

export function FilterBar({
  selectedCuisine,
  selectedPriceRange,
  onCuisineChange,
  onPriceRangeChange
}: FilterBarProps) {
  return (
    <div className="space-y-4">
      <div>
        <h3 className="text-sm font-semibold mb-3 text-gray-700">Cuisine Type</h3>
        <div className="flex flex-wrap gap-2">
          {cuisineCategories.map((cuisine) => (
            <Button
              key={cuisine}
              variant={selectedCuisine === cuisine ? "default" : "outline"}
              size="sm"
              onClick={() => onCuisineChange(cuisine)}
              className="rounded-full"
            >
              {cuisine}
            </Button>
          ))}
        </div>
      </div>
      
      <div>
        <h3 className="text-sm font-semibold mb-3 text-gray-700">Price Range</h3>
        <div className="flex gap-2">
          <Button
            variant={selectedPriceRange === "All" ? "default" : "outline"}
            size="sm"
            onClick={() => onPriceRangeChange("All")}
            className="rounded-full"
          >
            All
          </Button>
          {priceRanges.map((range) => (
            <Button
              key={range}
              variant={selectedPriceRange === range ? "default" : "outline"}
              size="sm"
              onClick={() => onPriceRangeChange(range)}
              className="rounded-full"
            >
              {range}
            </Button>
          ))}
        </div>
      </div>
    </div>
  );
}
