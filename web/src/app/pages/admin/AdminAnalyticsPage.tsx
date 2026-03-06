import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../../components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../components/ui/select";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line, AreaChart, Area } from "recharts";
import { TrendingUp, Users, DollarSign, Star } from "lucide-react";

export function AdminAnalyticsPage() {
  const monthlyRevenue = [
    { month: "Oct", revenue: 12400 },
    { month: "Nov", revenue: 15200 },
    { month: "Dec", revenue: 18600 },
    { month: "Jan", revenue: 22300 },
    { month: "Feb", revenue: 25800 },
    { month: "Mar", revenue: 29500 },
  ];

  const userGrowth = [
    { month: "Oct", users: 45 },
    { month: "Nov", users: 67 },
    { month: "Dec", users: 89 },
    { month: "Jan", users: 112 },
    { month: "Feb", users: 138 },
    { month: "Mar", users: 156 },
  ];

  const topRestaurants = [
    { name: "Prime Steakhouse", rating: 4.8, reviews: 423, revenue: 8500 },
    { name: "Sakura Sushi House", rating: 4.7, reviews: 456, revenue: 7800 },
    { name: "Brew & Beans", rating: 4.6, reviews: 189, revenue: 4200 },
    { name: "Dragon Noodle Bar", rating: 4.5, reviews: 298, revenue: 5600 },
    { name: "Bella Italia", rating: 4.5, reviews: 328, revenue: 6100 },
  ];

  const categoryPerformance = [
    { category: "Italian", orders: 245, avgRating: 4.5 },
    { category: "Japanese", orders: 198, avgRating: 4.7 },
    { category: "American", orders: 312, avgRating: 4.4 },
    { category: "Mexican", orders: 167, avgRating: 4.3 },
    { category: "Asian", orders: 189, avgRating: 4.5 },
  ];

  return (
    <div className="p-4 md:p-8 space-y-8">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold">Analytics</h1>
          <p className="text-gray-500 mt-1">Detailed performance insights</p>
        </div>
        <Select defaultValue="30">
          <SelectTrigger className="w-[180px]">
            <SelectValue placeholder="Select period" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="7">Last 7 days</SelectItem>
            <SelectItem value="30">Last 30 days</SelectItem>
            <SelectItem value="90">Last 90 days</SelectItem>
            <SelectItem value="365">Last year</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Total Revenue
            </CardTitle>
            <DollarSign className="size-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">$124,560</div>
            <p className="text-xs text-green-600 flex items-center gap-1 mt-1">
              <TrendingUp className="size-3" />
              +12.5% from last month
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Active Users
            </CardTitle>
            <Users className="size-4 text-blue-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">156</div>
            <p className="text-xs text-green-600 flex items-center gap-1 mt-1">
              <TrendingUp className="size-3" />
              +5.2% from last month
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Avg Order Value
            </CardTitle>
            <DollarSign className="size-4 text-orange-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">$42.50</div>
            <p className="text-xs text-green-600 flex items-center gap-1 mt-1">
              <TrendingUp className="size-3" />
              +8.1% from last month
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Platform Rating
            </CardTitle>
            <Star className="size-4 text-yellow-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">4.5</div>
            <p className="text-xs text-gray-600 flex items-center gap-1 mt-1">
              <Star className="size-3 fill-yellow-600 text-yellow-600" />
              Across all restaurants
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Revenue Trend</CardTitle>
            <CardDescription>Monthly revenue over time</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <AreaChart data={monthlyRevenue}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Area
                  type="monotone"
                  dataKey="revenue"
                  stroke="#f97316"
                  fill="#fed7aa"
                />
              </AreaChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>User Growth</CardTitle>
            <CardDescription>New users registered each month</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={userGrowth}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Line
                  type="monotone"
                  dataKey="users"
                  stroke="#3b82f6"
                  strokeWidth={2}
                />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Category Performance</CardTitle>
            <CardDescription>Orders by cuisine category</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={categoryPerformance}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="category" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="orders" fill="#f97316" />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Top Restaurants</CardTitle>
            <CardDescription>Best performing restaurants this month</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {topRestaurants.map((restaurant, index) => (
                <div key={index} className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="flex items-center justify-center size-8 bg-orange-100 text-orange-600 rounded-full font-bold">
                      {index + 1}
                    </div>
                    <div>
                      <p className="font-medium">{restaurant.name}</p>
                      <div className="flex items-center gap-2 text-sm text-gray-500">
                        <Star className="size-3 fill-orange-500 text-orange-500" />
                        <span>{restaurant.rating}</span>
                        <span>•</span>
                        <span>{restaurant.reviews} reviews</span>
                      </div>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="font-semibold text-green-600">
                      ${restaurant.revenue.toLocaleString()}
                    </p>
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
