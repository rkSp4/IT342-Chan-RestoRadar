import api from "./api";
import type { Pagination } from "./restaurantService";

// ── Reviews ───────────────────────────────────────────────────────────────────

export interface Review {
  id: string;
  userId: string;
  userName: string;
  rating: number;
  comment: string;
  photos?: string[];
  createdAt: string;
  updatedAt: string;
}

export interface ReviewRequest {
  rating: number;
  comment: string;
  photos?: string[];
}

export const reviewService = {
  getByRestaurant: async (restaurantId: string, sort = "recent", page = 1, limit = 10) => {
    const res = await api.get(`/restaurants/${restaurantId}/reviews`, {
      params: { sort, page, limit },
    });
    return res.data.data as { reviews: Review[]; pagination: Pagination };
  },

  add: async (restaurantId: string, data: ReviewRequest): Promise<Review> => {
    const res = await api.post(`/restaurants/${restaurantId}/reviews`, data);
    return res.data.data.review;
  },

  update: async (reviewId: string, data: ReviewRequest): Promise<Review> => {
    const res = await api.put(`/reviews/${reviewId}`, data);
    return res.data.data.review;
  },

  delete: async (reviewId: string): Promise<void> => {
    await api.delete(`/reviews/${reviewId}`);
  },

  getByUser: async (userId: string, page = 1, limit = 10) => {
    const res = await api.get(`/users/${userId}/reviews`, { params: { page, limit } });
    return res.data.data as { reviews: Review[]; pagination: Pagination };
  },
};

// ── Favorites ─────────────────────────────────────────────────────────────────

export interface Favorite {
  id: string;
  userId: string;
  restaurantId: string;
  restaurantName: string;
  restaurantPriceRange: string;
  restaurantRating: number;
  restaurantImageUrl?: string;
  createdAt: string;
}

export const favoriteService = {
  getByUser: async (userId: string, page = 1, limit = 20) => {
    const res = await api.get(`/users/${userId}/favorites`, { params: { page, limit } });
    return res.data.data as { favorites: Favorite[]; pagination: Pagination };
  },

  add: async (restaurantId: string): Promise<Favorite> => {
    const res = await api.post("/favorites", { restaurantId });
    return res.data.data.favorite;
  },

  remove: async (restaurantId: string): Promise<void> => {
    await api.delete(`/favorites/${restaurantId}`);
  },
};

// ── User Profile ──────────────────────────────────────────────────────────────

export interface UserProfile {
  id: string;
  email: string;
  fullName: string;
  profileImage?: string;
  role: string;
  joinDate: string;
  reviewCount: number;
  favoriteCount: number;
}

export interface UpdateProfileRequest {
  fullName?: string;
  email?: string;
  profileImage?: string;
}

export const userService = {
  getProfile: async (userId: string): Promise<UserProfile> => {
    const res = await api.get(`/users/${userId}`);
    return res.data.data.user;
  },

  updateProfile: async (userId: string, data: UpdateProfileRequest): Promise<UserProfile> => {
    const res = await api.put(`/users/${userId}`, data);
    return res.data.data.user;
  },

  deleteAccount: async (userId: string): Promise<void> => {
    await api.delete(`/users/${userId}`);
  },
};

// ── Admin ─────────────────────────────────────────────────────────────────────

export const adminService = {
  getUsers: async (page = 1, limit = 20) => {
    const res = await api.get("/admin/users", { params: { page, limit } });
    return res.data.data;
  },

  updateUserRole: async (userId: string, role: string) => {
    const res = await api.put(`/admin/users/${userId}/role`, { role });
    return res.data.data.user;
  },

  deleteUser: async (userId: string): Promise<void> => {
    await api.delete(`/admin/users/${userId}`);
  },

  getRestaurants: async (page = 1, limit = 20) => {
    const res = await api.get("/admin/restaurants", { params: { page, limit } });
    return res.data.data;
  },

  deleteRestaurant: async (restaurantId: string): Promise<void> => {
    await api.delete(`/admin/restaurants/${restaurantId}`);
  },

  deleteReview: async (reviewId: string): Promise<void> => {
    await api.delete(`/admin/reviews/${reviewId}`);
  },
};