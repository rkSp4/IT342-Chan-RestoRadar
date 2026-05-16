import { createContext, useContext, useState, ReactNode } from "react";

interface AdminContextType {
  isAdminAuthenticated: boolean;
  login: (email: string, password: string) => boolean;
  logout: () => void;
}

const AdminContext = createContext<AdminContextType | undefined>(undefined);

export function AdminProvider({ children }: { children: ReactNode }) {
  const [isAdminAuthenticated, setIsAdminAuthenticated] = useState<boolean>(() => {
    return localStorage.getItem("adminAuth") === "true";
  });

  const login = (email: string, password: string) => {
    // Mock authentication - in production, this would call an API
    if (email === "admin@foodspot.com" && password === "admin123") {
      setIsAdminAuthenticated(true);
      localStorage.setItem("adminAuth", "true");
      return true;
    }
    return false;
  };

  const logout = () => {
    setIsAdminAuthenticated(false);
    localStorage.removeItem("adminAuth");
  };

  return (
    <AdminContext.Provider value={{ isAdminAuthenticated, login, logout }}>
      {children}
    </AdminContext.Provider>
  );
}

export function useAdmin() {
  const context = useContext(AdminContext);
  if (context === undefined) {
    throw new Error("useAdmin must be used within an AdminProvider");
  }
  return context;
}
