import api from "./api";

export interface Restaurant {
  id: string;
  name: string;
  address: string;
  cuisineType: string;
  priceRange: string;
  averageRating: number;
  reviewCount: number;
  distance?: number;
  latitude: number;
  longitude: number;
  website?: string;
  contactNumber?: string;
  operatingHours?: string;
  photos?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface Pagination {
  page: number;
  limit: number;
  total: number;
  pages: number;
}

export interface RestaurantListResponse {
  restaurants: Restaurant[];
  pagination: Pagination;
}

export interface CreateRestaurantRequest {
  name: string;
  address: string;
  latitude: number;
  longitude: number;
  cuisineType?: string;
  priceRange?: string;
  website?: string;
  contactNumber?: string;
  operatingHours?: string;
  photos?: string;
}

export const restaurantService = {
  getAll: async (page = 1, limit = 20): Promise<RestaurantListResponse> => {
    const res = await api.get("/restaurants", { params: { page, limit } });
    return res.data.data;
  },

  getById: async (id: string): Promise<Restaurant> => {
    const res = await api.get(`/restaurants/${id}`);
    return res.data.data.restaurant;
  },

  search: async (query: string, page = 1, limit = 20): Promise<RestaurantListResponse> => {
    const res = await api.get("/restaurants/search", { params: { query, page, limit } });
    return res.data.data;
  },

  getNearby: async (
    lat: number,
    lng: number,
    radius = 5,
    priceRange?: string,
    page = 1,
    limit = 20
  ): Promise<RestaurantListResponse> => {
    const res = await api.get("/restaurants/nearby", {
      params: { lat, lng, radius, priceRange, page, limit },
    });
    return res.data.data;
  },

  create: async (data: CreateRestaurantRequest): Promise<Restaurant> => {
    const res = await api.post("/restaurants", data);
    return res.data.data.restaurant;
  },

  update: async (id: string, data: Partial<CreateRestaurantRequest>): Promise<Restaurant> => {
    const res = await api.put(`/restaurants/${id}`, data);
    return res.data.data.restaurant;
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/restaurants/${id}`);
  },
};