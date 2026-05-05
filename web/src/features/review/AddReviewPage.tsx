import { useState } from "react";
import { useParams, Link, useNavigate } from "react-router";
import { mockRestaurants } from "../restaurant/mockData";
import { StarRating } from "./StarRating";
import { Button } from "../../shared/components/ui/button";
import { Textarea } from "../../shared/components/ui/textarea";
import { ArrowLeft } from "lucide-react";
import { toast } from "sonner";

export function AddReviewPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const restaurant = mockRestaurants.find((r) => r.id === id);
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");

  if (!restaurant) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold mb-2">Restaurant not found</h2>
          <Link to="/explore">
            <Button>Back to Explore</Button>
          </Link>
        </div>
      </div>
    );
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (rating === 0) {
      toast.error("Please select a rating");
      return;
    }
    
    if (comment.trim() === "") {
      toast.error("Please write a review");
      return;
    }

    try {
      const response = await fetch(`/api/v1/restaurants/${id}/reviews`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${localStorage.getItem("restoradar_access_token")}`
        },
        body: JSON.stringify({ score: rating, comment: comment })
      });

      if (!response.ok) {
        throw new Error("Failed to submit review");
      }

      toast.success("Review submitted successfully!");
      navigate(`/restaurant/${id}`);
    } catch (error) {
      console.error("Error submitting review:", error);
      toast.error("Failed to submit review. Please try again later.");
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 pb-20 md:pb-6">
      <div className="max-w-2xl mx-auto px-4 md:px-6 py-6">
        <div className="mb-6">
          <Link to={`/restaurant/${id}`}>
            <Button variant="ghost" size="sm">
              <ArrowLeft size={16} className="mr-2" />
              Back
            </Button>
          </Link>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6">
          <h1 className="text-2xl font-bold mb-2">Write a Review</h1>
          <p className="text-gray-600 mb-6">for {restaurant.name}</p>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-semibold mb-3">
                Your Rating
              </label>
              <div className="flex justify-center md:justify-start">
                <StarRating
                  rating={rating}
                  size={32}
                  interactive
                  onRatingChange={setRating}
                />
              </div>
              {rating > 0 && (
                <p className="text-sm text-gray-600 mt-2">
                  {rating === 5 && "Excellent!"}
                  {rating === 4 && "Great!"}
                  {rating === 3 && "Good"}
                  {rating === 2 && "Fair"}
                  {rating === 1 && "Poor"}
                </p>
              )}
            </div>

            <div>
              <label htmlFor="comment" className="block text-sm font-semibold mb-2">
                Your Review
              </label>
              <Textarea
                id="comment"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                placeholder="Share your experience at this restaurant..."
                rows={6}
                className="w-full"
              />
              <p className="text-sm text-gray-500 mt-2">
                {comment.length} characters
              </p>
            </div>

            <div className="flex gap-3">
              <Button type="submit" className="flex-1">
                Submit Review
              </Button>
              <Link to={`/restaurant/${id}`} className="flex-1">
                <Button type="button" variant="outline" className="w-full">
                  Cancel
                </Button>
              </Link>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
