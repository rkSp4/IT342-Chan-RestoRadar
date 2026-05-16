import { useState, useEffect, cloneElement } from "react";
import { Outlet, useOutletContext, useMatches, Navigate } from "react-router";
import { Sidebar } from "./shared/components/Sidebar";
import { MobileBottomNav } from "./shared/components/MobileBottomNav";
import { MobileHeader } from "./shared/components/MobileHeader";
import { WelcomeDialog } from "./features/restaurant/WelcomeDialog";
import { ViewModeIndicator } from "./features/restaurant/ViewModeIndicator";
import { Toaster } from "./shared/components/ui/sonner";
import { useAuth } from "./features/auth/AuthContext";

interface RootContextType {
  favorites: string[];
  onToggleFavorite: (id: string) => void;
}

export function Root() {
  const { isAuthenticated } = useAuth();
  
  const [favorites, setFavorites] = useState<string[]>(() => {
    const saved = localStorage.getItem("favorites");
    return saved ? JSON.parse(saved) : ["2", "5"];
  });

  useEffect(() => {
    localStorage.setItem("favorites", JSON.stringify(favorites));
  }, [favorites]);

  const handleToggleFavorite = (id: string) => {
    setFavorites((prev) =>
      prev.includes(id) ? prev.filter((fav) => fav !== id) : [...prev, id]
    );
  };

  const context: RootContextType = {
    favorites,
    onToggleFavorite: handleToggleFavorite,
  };

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return (
    <div className="flex h-screen overflow-hidden">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <MobileHeader />
        <main className="flex-1 overflow-y-auto">
          <Outlet context={context} />
        </main>
      </div>
      <MobileBottomNav />
      <WelcomeDialog />
      <ViewModeIndicator />
      <Toaster />
    </div>
  );
}

export function useRootContext() {
  return useOutletContext<RootContextType>();
}