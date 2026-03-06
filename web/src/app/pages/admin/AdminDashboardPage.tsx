import { useState, useEffect } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../../components/ui/card";
import { 
  Store, 
  MessageSquare, 
  Users, 
  Star,
  TrendingUp,
  TrendingDown,
  DollarSign
} from "lucide-react";
import { mockRestaurants, mockReviews } from "../../data/mockData";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line, PieChart, Pie, Cell } from "recharts";

export function AdminDashboardPage() {
  const [stats, setStats] = useState({
    totalRestaurants: 0,
    totalReviews: 0,
    totalUsers: 0,
    averageRating: 0,
  });

  useEffect(() => {
    const avgRating = mockRestaurants.reduce((acc, r) => acc + r.rating, 0) / mockRestaurants.length;
    setStats({
      totalRestaurants: mockRestaurants.length,
      totalReviews: mockReviews.length,
      totalUsers: 156, // Mock data
      averageRating: Math.round(avgRating * 10) / 10,
    });
  }, []);

  const cuisineData = mockRestaurants.reduce((acc, restaurant) => {
    const existing = acc.find(item => item.name === restaurant.cuisine);
    if (existing) {
      existing.value += 1;
    } else {
      acc.push({ name: restaurant.cuisine, value: 1 });
    }
    return acc;
  }, [] as { name: string; value: number }[]);

  const ratingDistribution = [
    { rating: "5 Star", count: 45 },
    { rating: "4 Star", count: 68 },
    { rating: "3 Star", count: 23 },
    { rating: "2 Star", count: 8 },
    { rating: "1 Star", count: 4 },
  ];

  const monthlyData = [
    { month: "Jan", reviews: 45, restaurants: 5 },
    { month: "Feb", reviews: 52, restaurants: 6 },
    { month: "Mar", reviews: 68, restaurants: 8 },
  ];

  const COLORS = ["#f97316", "#fb923c", "#fdba74", "#fed7aa", "#ffedd5", "#fff7ed"];

  return (
    <div className="p-4 md:p-8 space-y-8">
      <div>
        <h1 className="text-3xl font-bold">Dashboard</h1>
        <p className="text-gray-500 mt-1">Overview of your FoodSpot platform</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Total Restaurants
            </CardTitle>
            <Store className="size-4 text-orange-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalRestaurants}</div>
            <p className="text-xs text-green-600 flex items-center gap-1 mt-1">
              <TrendingUp className="size-3" />
              +2 this month
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Total Reviews
            </CardTitle>
            <MessageSquare className="size-4 text-orange-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalReviews}</div>
            <p className="text-xs text-green-600 flex items-center gap-1 mt-1">
              <TrendingUp className="size-3" />
              +12 this week
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Total Users
            </CardTitle>
            <Users className="size-4 text-orange-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalUsers}</div>
            <p className="text-xs text-green-600 flex items-center gap-1 mt-1">
              <TrendingUp className="size-3" />
              +8 this week
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Average Rating
            </CardTitle>
            <Star className="size-4 text-orange-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.averageRating}</div>
            <p className="text-xs text-gray-600 flex items-center gap-1 mt-1">
              <Star className="size-3 fill-orange-500 text-orange-500" />
              Across all restaurants
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Monthly Growth</CardTitle>
            <CardDescription>Reviews and restaurants added over time</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={monthlyData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Line type="monotone" dataKey="reviews" stroke="#f97316" strokeWidth={2} />
                <Line type="monotone" dataKey="restaurants" stroke="#fb923c" strokeWidth={2} />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Rating Distribution</CardTitle>
            <CardDescription>Breakdown of review ratings</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={ratingDistribution}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="rating" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="count" fill="#f97316" />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Cuisine Distribution</CardTitle>
            <CardDescription>Restaurants by cuisine type</CardDescription>
          </CardHeader>
          <CardContent className="flex justify-center">
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={cuisineData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                  outerRadius={100}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {cuisineData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Recent Activity</CardTitle>
            <CardDescription>Latest platform updates</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {[
                { type: "review", user: "Sarah Johnson", action: "left a 5-star review", time: "2 hours ago" },
                { type: "restaurant", user: "Admin", action: "added Garden Bistro", time: "5 hours ago" },
                { type: "user", user: "Mike Chen", action: "joined FoodSpot", time: "1 day ago" },
                { type: "review", user: "Emily Rodriguez", action: "left a review", time: "1 day ago" },
                { type: "restaurant", user: "Admin", action: "updated Prime Steakhouse", time: "2 days ago" },
              ].map((activity, index) => (
                <div key={index} className="flex items-start gap-3 pb-3 border-b last:border-b-0">
                  <div className={`p-2 rounded-full ${
                    activity.type === "review" ? "bg-blue-100" :
                    activity.type === "restaurant" ? "bg-orange-100" :
                    "bg-green-100"
                  }`}>
                    {activity.type === "review" ? <MessageSquare className="size-4 text-blue-600" /> :
                     activity.type === "restaurant" ? <Store className="size-4 text-orange-600" /> :
                     <Users className="size-4 text-green-600" />}
                  </div>
                  <div className="flex-1">
                    <p className="text-sm">
                      <span className="font-medium">{activity.user}</span> {activity.action}
                    </p>
                    <p className="text-xs text-gray-500 mt-1">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
