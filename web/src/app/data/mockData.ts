import { Restaurant, Review, User } from "../types/restaurant";

export const mockRestaurants: Restaurant[] = [
  {
    id: "1",
    name: "Bella Italia",
    cuisine: "Italian",
    priceRange: "$$",
    rating: 4.5,
    reviewCount: 328,
    address: "123 Main Street, Downtown",
    distance: 0.5,
    image: "https://images.unsplash.com/photo-1692025690885-736a2cf8eae4?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxpdGFsaWFuJTIwcGl6emElMjByZXN0YXVyYW50fGVufDF8fHx8MTc3MjQyNTk3Nnww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
    hours: "11:00 AM - 10:00 PM",
    phone: "(555) 123-4567",
    description: "Authentic Italian cuisine with handmade pasta and wood-fired pizzas. Family-owned since 1985.",
    latitude: 40.7128,
    longitude: -74.0060,
    tags: ["Pizza", "Pasta", "Wine Bar", "Outdoor Seating"]
  },
  {
    id: "2",
    name: "Sakura Sushi House",
    cuisine: "Japanese",
    priceRange: "$$$",
    rating: 4.7,
    reviewCount: 456,
    address: "456 Oak Avenue, Midtown",
    distance: 1.2,
    image: "https://images.unsplash.com/photo-1681270527819-6b97ec33a67f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxzdXNoaSUyMGphcGFuZXNlJTIwcmVzdGF1cmFudHxlbnwxfHx8fDE3NzI0MjczODN8MA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
    hours: "12:00 PM - 11:00 PM",
    phone: "(555) 234-5678",
    description: "Fresh sushi and sashimi prepared by master chefs. Traditional Japanese dining experience.",
    latitude: 40.7580,
    longitude: -73.9855,
    tags: ["Sushi", "Sashimi", "Sake", "Omakase"]
  },
  {
    id: "3",
    name: "El Mariachi",
    cuisine: "Mexican",
    priceRange: "$",
    rating: 4.3,
    reviewCount: 234,
    address: "789 Elm Street, Westside",
    distance: 2.0,
    image: "https://images.unsplash.com/photo-1688845465690-e5ea24774fd5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtZXhpY2FuJTIwdGFjb3MlMjBmb29kfGVufDF8fHx8MTc3MjQ4NjE5OHww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
    hours: "10:00 AM - 9:00 PM",
    phone: "(555) 345-6789",
    description: "Authentic Mexican street food with fresh ingredients and bold flavors.",
    latitude: 40.7489,
    longitude: -73.9680,
    tags: ["Tacos", "Burritos", "Margaritas", "Vegetarian Options"]
  },
  {
    id: "4",
    name: "The Burger Joint",
    cuisine: "American",
    priceRange: "$$",
    rating: 4.4,
    reviewCount: 512,
    address: "321 Pine Road, Eastside",
    distance: 0.8,
    image: "https://images.unsplash.com/photo-1728836485840-93054eef0f2d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxidXJnZXIlMjBhbWVyaWNhbiUyMGRpbmVyfGVufDF8fHx8MTc3MjQ4MTkyMXww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
    hours: "11:30 AM - 10:30 PM",
    phone: "(555) 456-7890",
    description: "Gourmet burgers made with premium beef and creative toppings. Craft beer selection.",
    latitude: 40.7614,
    longitude: -73.9776,
    tags: ["Burgers", "Craft Beer", "Fries", "Late Night"]
  },
  {
    id: "5",
    name: "Brew & Beans",
    cuisine: "Cafe",
    priceRange: "$",
    rating: 4.6,
    reviewCount: 189,
    address: "654 Maple Lane, Arts District",
    distance: 1.5,
    image: "https://images.unsplash.com/photo-1642647916129-3909c75c0267?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxjb2ZmZWUlMjBjYWZlJTIwaW50ZXJpb3J8ZW58MXx8fHwxNzcyNDA2NzQ4fDA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
    hours: "7:00 AM - 8:00 PM",
    phone: "(555) 567-8901",
    description: "Artisanal coffee and fresh pastries in a cozy atmosphere. Perfect for remote work.",
    latitude: 40.7282,
    longitude: -73.9942,
    tags: ["Coffee", "Pastries", "WiFi", "Brunch"]
  },
  {
    id: "6",
    name: "Dragon Noodle Bar",
    cuisine: "Asian",
    priceRange: "$$",
    rating: 4.5,
    reviewCount: 298,
    address: "987 Cedar Street, Chinatown",
    distance: 1.8,
    image: "https://images.unsplash.com/photo-1575295126138-a7ee2fa40739?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxhc2lhbiUyMG5vb2RsZXMlMjByZXN0YXVyYW50fGVufDF8fHx8MTc3MjUwNTUyNnww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
    hours: "11:00 AM - 10:00 PM",
    phone: "(555) 678-9012",
    description: "Hand-pulled noodles and authentic Asian cuisine in a modern setting.",
    latitude: 40.7158,
    longitude: -73.9970,
    tags: ["Noodles", "Ramen", "Dumplings", "Spicy"]
  },
  {
    id: "7",
    name: "Prime Steakhouse",
    cuisine: "Steakhouse",
    priceRange: "$$$$",
    rating: 4.8,
    reviewCount: 423,
    address: "147 Broadway, Financial District",
    distance: 2.3,
    image: "https://images.unsplash.com/photo-1731407321637-de010c69ceef?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxzdGVha2hvdXNlJTIwZmluZSUyMGRpbmluZ3xlbnwxfHx8fDE3NzI1Mjc3Mjh8MA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
    hours: "5:00 PM - 11:00 PM",
    phone: "(555) 789-0123",
    description: "Premium cuts of beef and fine wines in an upscale dining environment.",
    latitude: 40.7074,
    longitude: -74.0113,
    tags: ["Steak", "Wine", "Fine Dining", "Reservations"]
  },
  {
    id: "8",
    name: "Garden Bistro",
    cuisine: "Vegetarian",
    priceRange: "$$",
    rating: 4.4,
    reviewCount: 267,
    address: "258 Park Place, Greenpoint",
    distance: 1.1,
    image: "https://images.unsplash.com/photo-1768697358705-c1b60333da35?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxyZXN0YXVyYW50JTIwaW50ZXJpb3IlMjBlbGVnYW50fGVufDF8fHx8MTc3MjQzNDAxNXww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
    hours: "10:00 AM - 9:00 PM",
    phone: "(555) 890-1234",
    description: "Farm-to-table vegetarian and vegan cuisine with organic ingredients.",
    latitude: 40.7295,
    longitude: -73.9501,
    tags: ["Vegetarian", "Vegan", "Organic", "Gluten-Free"]
  }
];

export const mockReviews: Review[] = [
  {
    id: "r1",
    restaurantId: "1",
    userName: "Sarah Johnson",
    userAvatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah",
    rating: 5,
    date: "2026-02-28",
    comment: "Amazing authentic Italian food! The pasta was perfectly cooked and the sauce was incredible. Will definitely be back!",
    images: []
  },
  {
    id: "r2",
    restaurantId: "1",
    userName: "Mike Chen",
    userAvatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Mike",
    rating: 4,
    date: "2026-02-25",
    comment: "Great pizza and friendly staff. The only downside was the wait time, but it was worth it.",
    images: []
  },
  {
    id: "r3",
    restaurantId: "2",
    userName: "Emily Rodriguez",
    userAvatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Emily",
    rating: 5,
    date: "2026-03-01",
    comment: "Best sushi in town! Fresh fish and beautiful presentation. The chef's special roll was outstanding.",
    images: []
  },
  {
    id: "r4",
    restaurantId: "2",
    userName: "David Park",
    userAvatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=David",
    rating: 5,
    date: "2026-02-27",
    comment: "Incredible omakase experience. Every piece was perfect. Highly recommend!",
    images: []
  },
  {
    id: "r5",
    restaurantId: "3",
    userName: "Maria Garcia",
    userAvatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Maria",
    rating: 4,
    date: "2026-02-29",
    comment: "Delicious tacos and great value. The salsa bar is amazing with so many options!",
    images: []
  }
];

export const mockUser: User = {
  id: "u1",
  name: "Alex Thompson",
  email: "alex@example.com",
  avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Alex",
  favoriteRestaurants: ["2", "5"]
};

export const cuisineCategories = [
  "All",
  "Italian",
  "Japanese",
  "Mexican",
  "American",
  "Cafe",
  "Asian",
  "Steakhouse",
  "Vegetarian"
];

export const priceRanges = ["$", "$$", "$$$", "$$$$"];
