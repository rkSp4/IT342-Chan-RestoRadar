import { createBrowserRouter, Navigate } from "react-router";
import { LoginPage } from "./features/auth/LoginPage";
import { RegisterPage } from "./features/auth/RegisterPage";
import { HomePage } from "./features/restaurant/HomePage";
import { ExplorePage } from "./features/restaurant/ExplorePage";
import { OAuthCallbackPage } from "./features/auth/OAuthCallbackPage";
import { Root } from "./Root";
import { RestaurantDetailPage } from "./features/restaurant/RestaurantDetailPage";
import { FavoritesPage } from "./features/favorites/FavoritesPage";
import { MapPage } from "./features/restaurant/MapPage";
import { AddReviewPage } from "./features/review/AddReviewPage";
import { ProfilePage } from "./features/user/ProfilePage";
import { HelpPage } from "./features/help/HelpPage";
import { AdminProvider } from "./features/admin/AdminContext";
import { AdminLayout } from "./features/admin/AdminLayout";
import { AdminLoginPage } from "./features/admin/AdminLoginPage";
import { AdminDashboardPage } from "./features/admin/AdminDashboardPage";
import { AdminUsersPage } from "./features/admin/AdminUsersPage";
import { AdminRestaurantsPage } from "./features/admin/AdminRestaurantsPage";
import { AdminReviewsPage } from "./features/admin/AdminReviewsPage";
import { AdminAnalyticsPage } from "./features/admin/AdminAnalyticsPage";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      { index: true, element: <Navigate to="/login" replace /> },
      { path: "home", element: <HomePage /> },
      { path: "explore", element: <ExplorePage /> },
      { path: "restaurant/:id", element: <RestaurantDetailPage /> },
      { path: "favorites", element: <FavoritesPage /> },
      { path: "map", element: <MapPage /> },
      { path: "restaurant/:id/review", element: <AddReviewPage /> },
      { path: "profile", element: <ProfilePage /> },
      { path: "help", element: <HelpPage /> },
    ],
  },
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    path: "/register",
    element: <RegisterPage />,
  },
  {
    path: "/oauth/callback",
    element: <OAuthCallbackPage />,
  },
  {
  path: "/admin/login",
  element: (
    <AdminProvider>
      <AdminLoginPage />
    </AdminProvider>
  ),
  },
  {
    path: "/admin",
    element: (
      <AdminProvider>
        <AdminLayout />
      </AdminProvider>
    ),
    children: [
      { index: true, element: <Navigate to="/admin/dashboard" replace /> },
      { path: "dashboard", element: <AdminDashboardPage /> },
      { path: "users", element: <AdminUsersPage /> },
      { path: "restaurants", element: <AdminRestaurantsPage /> },
      { path: "reviews", element: <AdminReviewsPage /> },
      { path: "analytics", element: <AdminAnalyticsPage /> },
    ],
  },
]);