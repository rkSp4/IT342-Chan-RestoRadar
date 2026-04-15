import { createBrowserRouter, Navigate } from "react-router";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { HomePage } from "./pages/HomePage";
import { ExplorePage } from "./pages/ExplorePage";
import { OAuthCallbackPage } from "./pages/OAuthCallbackPage";
import { Root } from "./Root";
import { RestaurantDetailPage } from "./pages/RestaurantDetailPage";
import { FavoritesPage } from "./pages/FavoritesPage";
import { MapPage } from "./pages/MapPage";
import { AddReviewPage } from "./pages/AddReviewPage";
import { ProfilePage } from "./pages/ProfilePage";
import { HelpPage } from "./pages/HelpPage";

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