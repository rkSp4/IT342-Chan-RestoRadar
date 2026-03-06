import { Home, Map, Heart, User } from "lucide-react";
import { Link, useLocation } from "react-router";

export function MobileBottomNav() {
  const location = useLocation();
  
  const navItems = [
    { icon: Home, label: "Explore", path: "/" },
    { icon: Map, label: "Map", path: "/map" },
    { icon: Heart, label: "Favorites", path: "/favorites" },
    { icon: User, label: "Profile", path: "/profile" }
  ];
  
  return (
    <nav className="md:hidden fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 z-50">
      <div className="flex justify-around items-center h-16">
        {navItems.map((item) => {
          const isActive = location.pathname === item.path;
          const Icon = item.icon;
          
          return (
            <Link
              key={item.path}
              to={item.path}
              className={`flex flex-col items-center justify-center flex-1 h-full ${
                isActive ? "text-blue-600" : "text-gray-600"
              }`}
            >
              <Icon size={24} />
              <span className="text-xs mt-1">{item.label}</span>
            </Link>
          );
        })}
      </div>
    </nav>
  );
}
