import { mockReviews } from "../data/mockData";
import { User, Mail, Heart, MessageSquare, Settings, Shield, LogOut } from "lucide-react";
import { Button } from "../components/ui/button";
import { StarRating } from "../components/StarRating";
import { Link, useNavigate } from "react-router";
import { useAuth } from "../contexts/AuthContext";

export function ProfilePage() {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  // Redirect to login if not authenticated
  if (!isAuthenticated || !user) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
        <div className="bg-white rounded-lg shadow-md p-8 max-w-md w-full text-center">
          <User size={64} className="mx-auto mb-4 text-gray-300" />
          <h2 className="text-2xl font-bold mb-2">Sign In Required</h2>
          <p className="text-gray-600 mb-6">
            Please sign in to view your profile and manage your account.
          </p>
          <div className="flex gap-3">
            <Link to="/login" className="flex-1">
              <Button className="w-full">Sign In</Button>
            </Link>
            <Link to="/register" className="flex-1">
              <Button variant="outline" className="w-full">Create Account</Button>
            </Link>
          </div>
        </div>
      </div>
    );
  }

  const userReviews = mockReviews.filter((r) => r.userName === user.name);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <div className="min-h-screen bg-gray-50 pb-20 md:pb-6">
      <div className="max-w-4xl mx-auto px-4 md:px-6 py-6">
        {/* Profile Header */}
        <div className="bg-white rounded-lg shadow-md p-6 mb-6">
          <div className="flex flex-col md:flex-row items-center md:items-start gap-6">
            <img
              src={user.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${user.email}`}
              alt={user.name}
              className="w-24 h-24 rounded-full border-4 border-orange-100"
            />
            <div className="flex-1 text-center md:text-left">
              <h1 className="text-2xl font-bold mb-2">{user.name}</h1>
              <div className="flex items-center justify-center md:justify-start gap-2 text-gray-600 mb-2">
                <Mail size={16} />
                <span>{user.email}</span>
              </div>
              {user.phone && (
                <div className="text-gray-600 mb-4">
                  {user.phone}
                </div>
              )}
              <div className="flex flex-wrap gap-4 justify-center md:justify-start">
                <div className="text-center">
                  <div className="text-2xl font-bold text-orange-600">0</div>
                  <div className="text-sm text-gray-600">Favorites</div>
                </div>
                <div className="text-center">
                  <div className="text-2xl font-bold text-orange-600">{userReviews.length}</div>
                  <div className="text-sm text-gray-600">Reviews</div>
                </div>
                <div className="text-center">
                  <div className="text-2xl font-bold text-orange-600">0</div>
                  <div className="text-sm text-gray-600">Check-ins</div>
                </div>
              </div>
            </div>
            <div className="flex gap-2">
              <Button variant="outline">
                <Settings size={16} className="mr-2" />
                Edit Profile
              </Button>
              <Button variant="outline" onClick={handleLogout}>
                <LogOut size={16} className="mr-2" />
                Logout
              </Button>
            </div>
          </div>
          
          {/* Admin Access */}
          <div className="mt-6 pt-6 border-t border-gray-200">
            <Link to="/admin/login">
              <Button variant="outline" className="w-full md:w-auto">
                <Shield size={16} className="mr-2" />
                Admin Dashboard
              </Button>
            </Link>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <div className="bg-white rounded-lg shadow-md p-6 text-center">
            <div className="bg-red-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
              <Heart className="text-red-600" size={24} />
            </div>
            <h3 className="font-semibold mb-1">Favorite Cuisine</h3>
            <p className="text-gray-600">Not set yet</p>
          </div>
          
          <div className="bg-white rounded-lg shadow-md p-6 text-center">
            <div className="bg-orange-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
              <MessageSquare className="text-orange-600" size={24} />
            </div>
            <h3 className="font-semibold mb-1">Total Reviews</h3>
            <p className="text-gray-600">{userReviews.length} written</p>
          </div>
          
          <div className="bg-white rounded-lg shadow-md p-6 text-center">
            <div className="bg-green-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
              <User className="text-green-600" size={24} />
            </div>
            <h3 className="font-semibold mb-1">Member Since</h3>
            <p className="text-gray-600">
              {new Date(user.joinedDate).toLocaleDateString("en-US", {
                month: "long",
                year: "numeric"
              })}
            </p>
          </div>
        </div>

        {/* Recent Reviews */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-xl font-bold mb-6">Your Recent Reviews</h2>
          
          {userReviews.length > 0 ? (
            <div className="space-y-6">
              {userReviews.map((review) => (
                <div key={review.id} className="border-b border-gray-200 pb-6 last:border-b-0 last:pb-0">
                  <div className="flex items-center justify-between mb-3">
                    <h3 className="font-semibold">Restaurant Review</h3>
                    <StarRating rating={review.rating} size={16} />
                  </div>
                  <p className="text-gray-700 mb-2">{review.comment}</p>
                  <p className="text-sm text-gray-500">
                    {new Date(review.date).toLocaleDateString("en-US", {
                      year: "numeric",
                      month: "long",
                      day: "numeric"
                    })}
                  </p>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              <MessageSquare size={48} className="mx-auto mb-4 text-gray-300" />
              <p>You haven't written any reviews yet</p>
              <Link to="/explore">
                <Button className="mt-4">Explore Restaurants</Button>
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}