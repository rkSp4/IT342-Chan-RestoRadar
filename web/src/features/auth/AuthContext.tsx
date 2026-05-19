import { createContext, useContext, useState, ReactNode, useEffect, useCallback } from "react";

export interface User {
  id: string;
  fullName: string;
  email: string;
  profileImage?: string;
  role: string;
  createdAt: string;
}

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<{ success: boolean; error?: string }>;
  register: (name: string, email: string, password: string, confirmPassword: string) => Promise<{ success: boolean; error?: string }>;
  logout: () => void;
  updateProfile: (updates: Partial<User>) => void;
  oauthLogin: (user: User, accessToken: string, refreshToken: string) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const API_BASE = `${import.meta.env.VITE_API_URL}/auth`;

// Token keys — used consistently across AuthContext and api.ts
export const TOKEN_KEY = "restoradar_access_token";
export const REFRESH_TOKEN_KEY = "restoradar_refresh_token";
export const USER_KEY = "restoradar_user";

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(() => {
    const savedUser = localStorage.getItem(USER_KEY);
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const isAuthenticated = user !== null;

  const authContextLogin = useCallback((authUser: User, accessToken: string, refreshToken: string) => {
    localStorage.setItem(TOKEN_KEY, accessToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
    localStorage.setItem(USER_KEY, JSON.stringify(authUser));
    setUser(authUser);
  }, []);

  const login = async (email: string, password: string): Promise<{ success: boolean; error?: string }> => {
    try {
      const response = await fetch(`${API_BASE}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      const json = await response.json();

      if (!response.ok) {
        return { success: false, error: json.error?.message || "Invalid email or password" };
      }

      // Backend wraps response: { success, data: { user, token, refreshToken } }
      const { user: authUser, accessToken, refreshToken } = json;

      if (!accessToken || !authUser) {
        return { success: false, error: json.error?.message || "Registration failed" };
      }

      authContextLogin(authUser, accessToken, refreshToken);
      return { success: true };
    } catch {
      return { success: false, error: "Network error. Please try again." };
    }
  };

  const register = async (
    name: string,
    email: string,
    password: string,
    confirmPassword: string
  ): Promise<{ success: boolean; error?: string }> => {
    try {
      const response = await fetch(`${API_BASE}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ fullName: name, email, password, confirmPassword }),
      });

      const json = await response.json();

      if (!response.ok) {
        return { success: false, error: json.error?.message || "Registration failed" };
      }

      const { user: authUser, accessToken, refreshToken } = json;

      if (!accessToken || !authUser) {
        return { success: false, error: json.error?.message || "Registration failed" };
      }

      authContextLogin(authUser, accessToken, refreshToken);
      return { success: true };
    } catch {
      return { success: false, error: "Network error. Please try again." };
    }
  };

  const logout = () => {
    // Fire and forget — don't block UI on logout API call
    fetch(`${API_BASE}/logout`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${localStorage.getItem(TOKEN_KEY)}`,
      },
    }).catch(() => {});

    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    setUser(null);
  };

  const updateProfile = (updates: Partial<User>) => {
    if (user) {
      setUser({ ...user, ...updates });
    }
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, login, register, logout, updateProfile, oauthLogin: authContextLogin }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}