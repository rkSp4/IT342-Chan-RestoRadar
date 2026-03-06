import { useState } from "react";
import { Card } from "../../components/ui/card";
import { Button } from "../../components/ui/button";
import { Input } from "../../components/ui/input";
import { Badge } from "../../components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../../components/ui/table";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../components/ui/select";
import { mockReviews, mockRestaurants } from "../../data/mockData";
import { Review } from "../../types/restaurant";
import { Search, Trash2, Star, CheckCircle, XCircle } from "lucide-react";
import { toast } from "sonner";

export function AdminReviewsPage() {
  const [reviews, setReviews] = useState<Review[]>(mockReviews);
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<"all" | "approved" | "pending">("all");

  const getRestaurantName = (restaurantId: string) => {
    return mockRestaurants.find((r) => r.id === restaurantId)?.name || "Unknown";
  };

  const filteredReviews = reviews.filter((review) => {
    const matchesSearch =
      review.userName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      review.comment.toLowerCase().includes(searchQuery.toLowerCase()) ||
      getRestaurantName(review.restaurantId).toLowerCase().includes(searchQuery.toLowerCase());

    return matchesSearch;
  });

  const handleDelete = (id: string) => {
    setReviews(reviews.filter((r) => r.id !== id));
    toast.success("Review deleted successfully");
  };

  const renderStars = (rating: number) => {
    return (
      <div className="flex gap-0.5">
        {[...Array(5)].map((_, i) => (
          <Star
            key={i}
            className={`size-4 ${
              i < rating ? "fill-orange-500 text-orange-500" : "text-gray-300"
            }`}
          />
        ))}
      </div>
    );
  };

  return (
    <div className="p-4 md:p-8 space-y-6">
      <div>
        <h1 className="text-3xl font-bold">Reviews</h1>
        <p className="text-gray-500 mt-1">Manage customer reviews</p>
      </div>

      <div className="flex flex-col md:flex-row gap-4">
        <Card className="flex-1 p-4">
          <div className="flex items-center gap-2">
            <Search className="size-5 text-gray-400" />
            <Input
              placeholder="Search reviews..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="border-0 focus-visible:ring-0"
            />
          </div>
        </Card>
        <Select value={statusFilter} onValueChange={(value: any) => setStatusFilter(value)}>
          <SelectTrigger className="w-full md:w-[180px]">
            <SelectValue placeholder="Filter by status" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Reviews</SelectItem>
            <SelectItem value="approved">Approved</SelectItem>
            <SelectItem value="pending">Pending</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <Card>
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>User</TableHead>
                <TableHead>Restaurant</TableHead>
                <TableHead>Rating</TableHead>
                <TableHead>Comment</TableHead>
                <TableHead>Date</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredReviews.map((review) => (
                <TableRow key={review.id}>
                  <TableCell>
                    <div className="flex items-center gap-3">
                      <img
                        src={review.userAvatar}
                        alt={review.userName}
                        className="size-10 rounded-full"
                      />
                      <span className="font-medium">{review.userName}</span>
                    </div>
                  </TableCell>
                  <TableCell>
                    <span className="font-medium">
                      {getRestaurantName(review.restaurantId)}
                    </span>
                  </TableCell>
                  <TableCell>{renderStars(review.rating)}</TableCell>
                  <TableCell>
                    <div className="max-w-md truncate text-sm text-gray-600">
                      {review.comment}
                    </div>
                  </TableCell>
                  <TableCell>
                    <span className="text-sm text-gray-600">
                      {new Date(review.date).toLocaleDateString()}
                    </span>
                  </TableCell>
                  <TableCell className="text-right">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleDelete(review.id)}
                    >
                      <Trash2 className="size-4 text-red-600" />
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </Card>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Total Reviews</p>
              <p className="text-2xl font-bold mt-1">{reviews.length}</p>
            </div>
            <div className="bg-blue-100 p-3 rounded-full">
              <Star className="size-6 text-blue-600" />
            </div>
          </div>
        </Card>

        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Average Rating</p>
              <p className="text-2xl font-bold mt-1">
                {(reviews.reduce((acc, r) => acc + r.rating, 0) / reviews.length).toFixed(1)}
              </p>
            </div>
            <div className="bg-orange-100 p-3 rounded-full">
              <Star className="size-6 text-orange-600 fill-orange-600" />
            </div>
          </div>
        </Card>

        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">This Week</p>
              <p className="text-2xl font-bold mt-1">12</p>
            </div>
            <div className="bg-green-100 p-3 rounded-full">
              <CheckCircle className="size-6 text-green-600" />
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}
