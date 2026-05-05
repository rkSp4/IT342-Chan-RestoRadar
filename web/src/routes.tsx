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

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Navigate to="/explore" replace />,
  },
  {
    path: "/",
    element: <Root />,
    children: [
      { path: "/home", element: <HomePage /> },
      { path: "/explore", element: <ExplorePage /> },
      { path: "/restaurant/:id", element: <RestaurantDetailPage /> },
      { path: "/favorites", element: <FavoritesPage /> },
      { path: "/map", element: <MapPage /> },
      { path: "/restaurant/:id/review", element: <AddReviewPage /> },
      { path: "/profile", element: <ProfilePage /> },
      { path: "/help", element: <HelpPage /> },
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
]);