import { Review } from "../types/restaurant";
import { StarRating } from "./StarRating";

interface ReviewCardProps {
  review: Review;
}

export function ReviewCard({ review }: ReviewCardProps) {
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric"
    });
  };

  return (
    <div className="border-b border-gray-200 pb-6 mb-6 last:border-b-0">
      <div className="flex items-start gap-3">
        <img
          src={review.userAvatar}
          alt={review.userName}
          className="w-12 h-12 rounded-full"
        />
        <div className="flex-1">
          <div className="flex items-center justify-between mb-2">
            <div>
              <h4 className="font-semibold">{review.userName}</h4>
              <p className="text-sm text-gray-500">{formatDate(review.date)}</p>
            </div>
            <StarRating rating={review.rating} size={16} />
          </div>
          <p className="text-gray-700">{review.comment}</p>
          {review.images && review.images.length > 0 && (
            <div className="flex gap-2 mt-3">
              {review.images.map((image, index) => (
                <img
                  key={index}
                  src={image}
                  alt={`Review ${index + 1}`}
                  className="w-20 h-20 object-cover rounded-md"
                />
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}