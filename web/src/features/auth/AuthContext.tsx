import { createContext, useContext, useState, ReactNode, useEffect } from "react";

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
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const API_BASE = "/api/v1/auth";

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(() => {
    const savedUser = localStorage.getItem("restoradar_user");
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const isAuthenticated = user !== null;

  useEffect(() => {
    if (user) {
      localStorage.setItem("restoradar_user", JSON.stringify(user));
    } else {
      localStorage.removeItem("restoradar_user");
      localStorage.removeItem("restoradar_access_token");
      localStorage.removeItem("restoradar_refresh_token");
    }
  }, [user]);

  const login = async (email: string, password: string): Promise<{ success: boolean; error?: string }> => {
    try {
      const response = await fetch(`${API_BASE}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (!response.ok) {
        return { success: false, error: data.error?.message || "Invalid email or password" };
      }

      localStorage.setItem("restoradar_access_token", data.accessToken);
      localStorage.setItem("restoradar_refresh_token", data.refreshToken);
      setUser(data.user);
      return { success: true };
    } catch (error) {
      return { success: false, error: "Network error. Please try again." };
    }
  };

  const register = async (name: string, email: string, password: string, confirmPassword: string): Promise<{ success: boolean; error?: string }> => {
    try {
      const response = await fetch(`${API_BASE}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ 
          fullName: name, 
          email, 
          password,
          confirmPassword 
        }),
      });

      const data = await response.json();

      if (!response.ok) {
        return { success: false, error: data.error?.message || "Registration failed" };
      }

      localStorage.setItem("restoradar_access_token", data.accessToken);
      localStorage.setItem("restoradar_refresh_token", data.refreshToken);
      setUser(data.user);
      return { success: true };
    } catch (error) {
      return { success: false, error: "Network error. Please try again." };
    }
  };

  const logout = () => {
    setUser(null);
  };

  const updateProfile = (updates: Partial<User>) => {
    if (user) {
      const updatedUser = { ...user, ...updates };
      setUser(updatedUser);
    }
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, login, register, logout, updateProfile }}>
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