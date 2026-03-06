export interface Restaurant {
  id: string;
  name: string;
  cuisine: string;
  priceRange: "$" | "$$" | "$$$" | "$$$$";
  rating: number;
  reviewCount: number;
  address: string;
  distance: number; // in miles
  image: string;
  hours: string;
  phone: string;
  description: string;
  latitude: number;
  longitude: number;
  tags: string[];
}

export interface Review {
  id: string;
  restaurantId: string;
  userName: string;
  userAvatar: string;
  rating: number;
  date: string;
  comment: string;
  images?: string[];
}

export interface User {
  id: string;
  name: string;
  email: string;
  avatar: string;
  favoriteRestaurants: string[];
}
