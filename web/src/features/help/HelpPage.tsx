import { Card } from "../../shared/components/ui/card";
import { Button } from "../../shared/components/ui/button";
import { 
  Search, 
  Heart, 
  Map, 
  User, 
  Star, 
  MessageSquare,
  Shield,
  Smartphone,
  Monitor
} from "lucide-react";
import { Link } from "react-router";

export function HelpPage() {
  return (
    <div className="min-h-screen bg-gray-50 pb-20 md:pb-6">
      <div className="max-w-4xl mx-auto px-4 md:px-6 py-6">
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-2">How to Use FoodSpot</h1>
          <p className="text-gray-600">
            Your complete guide to discovering great food and managing your platform
          </p>
        </div>

        {/* User Features */}
        <div className="mb-8">
          <h2 className="text-2xl font-bold mb-4 flex items-center gap-2">
            <Monitor className="size-6 text-orange-600" />
            User Features
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Card className="p-6">
              <div className="flex items-start gap-4">
                <div className="bg-orange-100 p-3 rounded-lg">
                  <Search className="size-6 text-orange-600" />
                </div>
                <div>
                  <h3 className="font-semibold mb-2">Explore Restaurants</h3>
                  <p className="text-sm text-gray-600">
                    Browse restaurants, use filters for cuisine and price range, and search by name or location.
                  </p>
                </div>
              </div>
            </Card>

            <Card className="p-6">
              <div className="flex items-start gap-4">
                <div className="bg-red-100 p-3 rounded-lg">
                  <Heart className="size-6 text-red-600" />
                </div>
                <div>
                  <h3 className="font-semibold mb-2">Save Favorites</h3>
                  <p className="text-sm text-gray-600">
                    Click the heart icon on any restaurant to save it to your favorites for quick access later.
                  </p>
                </div>
              </div>
            </Card>

            <Card className="p-6">
              <div className="flex items-start gap-4">
                <div className="bg-blue-100 p-3 rounded-lg">
                  <Map className="size-6 text-blue-600" />
                </div>
                <div>
                  <h3 className="font-semibold mb-2">View on Map</h3>
                  <p className="text-sm text-gray-600">
                    See all restaurants on an interactive map to find options near your location.
                  </p>
                </div>
              </div>
            </Card>

            <Card className="p-6">
              <div className="flex items-start gap-4">
                <div className="bg-yellow-100 p-3 rounded-lg">
                  <Star className="size-6 text-yellow-600" />
                </div>
                <div>
                  <h3 className="font-semibold mb-2">Rate & Review</h3>
                  <p className="text-sm text-gray-600">
                    Share your experience by leaving star ratings and written reviews for restaurants you visit.
                  </p>
                </div>
              </div>
            </Card>
          </div>
        </div>

        {/* Admin Features */}
        <div className="mb-8">
          <h2 className="text-2xl font-bold mb-4 flex items-center gap-2">
            <Shield className="size-6 text-blue-600" />
            Admin Dashboard
          </h2>
          <Card className="p-6 mb-4">
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-4">
              <h3 className="font-semibold mb-2">Access Admin Dashboard</h3>
              <p className="text-sm text-gray-600 mb-3">
                Login with demo credentials to access the full admin panel:
              </p>
              <div className="bg-white p-3 rounded border border-blue-200">
                <p className="text-sm"><strong>Email:</strong> admin@foodspot.com</p>
                <p className="text-sm"><strong>Password:</strong> admin123</p>
              </div>
              <Link to="/admin/login" className="mt-4 block">
                <Button className="w-full">
                  <Shield className="size-4 mr-2" />
                  Go to Admin Login
                </Button>
              </Link>
            </div>
          </Card>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Card className="p-6">
              <h3 className="font-semibold mb-2">Restaurant Management</h3>
              <p className="text-sm text-gray-600">
                Add, edit, and remove restaurant listings. Update photos, descriptions, hours, and contact information.
              </p>
            </Card>

            <Card className="p-6">
              <h3 className="font-semibold mb-2">Review Moderation</h3>
              <p className="text-sm text-gray-600">
                Monitor and manage user reviews. Delete inappropriate content and maintain quality standards.
              </p>
            </Card>

            <Card className="p-6">
              <h3 className="font-semibold mb-2">User Management</h3>
              <p className="text-sm text-gray-600">
                View user activity, manage accounts, and monitor platform engagement metrics.
              </p>
            </Card>

            <Card className="p-6">
              <h3 className="font-semibold mb-2">Analytics & Insights</h3>
              <p className="text-sm text-gray-600">
                Access detailed charts and statistics on platform performance, user growth, and trends.
              </p>
            </Card>
          </div>
        </div>

        {/* Mobile Guide */}
        <div>
          <h2 className="text-2xl font-bold mb-4 flex items-center gap-2">
            <Smartphone className="size-6 text-green-600" />
            Mobile Experience
          </h2>
          <Card className="p-6">
            <div className="space-y-4">
              <div>
                <h3 className="font-semibold mb-2">Navigation</h3>
                <p className="text-sm text-gray-600 mb-2">
                  On mobile devices, use the bottom navigation bar to switch between main sections:
                </p>
                <ul className="text-sm text-gray-600 space-y-1 ml-4">
                  <li>• <strong>Explore:</strong> Browse all restaurants</li>
                  <li>• <strong>Favorites:</strong> Your saved restaurants</li>
                  <li>• <strong>Map:</strong> View locations</li>
                  <li>• <strong>Profile:</strong> Your account and reviews</li>
                </ul>
              </div>
              <div>
                <h3 className="font-semibold mb-2">Mobile Admin</h3>
                <p className="text-sm text-gray-600">
                  The admin dashboard is fully optimized for mobile with touch-friendly controls and a bottom navigation bar.
                </p>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </div>
  );
}
