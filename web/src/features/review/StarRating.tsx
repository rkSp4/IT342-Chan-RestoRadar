import { Star } from "lucide-react";

interface StarRatingProps {
  rating: number;
  maxRating?: number;
  size?: number;
  showNumber?: boolean;
  interactive?: boolean;
  onRatingChange?: (rating: number) => void;
}

export function StarRating({
  rating,
  maxRating = 5,
  size = 16,
  showNumber = false,
  interactive = false,
  onRatingChange
}: StarRatingProps) {
  const handleStarClick = (index: number) => {
    if (interactive && onRatingChange) {
      onRatingChange(index + 1);
    }
  };

  return (
    <div className="flex items-center gap-1">
      {Array.from({ length: maxRating }, (_, index) => {
        const fillPercentage = Math.min(Math.max(rating - index, 0), 1);
        
        return (
          <div
            key={index}
            className="relative"
            onClick={() => handleStarClick(index)}
            style={{ cursor: interactive ? 'pointer' : 'default' }}
          >
            <Star
              size={size}
              className="text-gray-300"
              fill="currentColor"
            />
            <div
              className="absolute top-0 left-0 overflow-hidden"
              style={{ width: `${fillPercentage * 100}%` }}
            >
              <Star
                size={size}
                className="text-yellow-400"
                fill="currentColor"
              />
            </div>
          </div>
        );
      })}
      {showNumber && (
        <span className="ml-1 text-sm text-gray-600">
          {rating.toFixed(1)}
        </span>
      )}
    </div>
  );
}
