import { Home, Map, Heart, User, Utensils, LogIn, UserPlus, LogOut } from "lucide-react";
import { Link, useLocation, useNavigate } from "react-router";
import { useAuth } from "../../features/auth/AuthContext";
import { Button } from "../../shared/components/ui/button";

export function Sidebar() {
  const location = useLocation();
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();
  
  const navItems = [
    { icon: Home, label: "Explore", path: "/explore" },
    { icon: Map, label: "Map View", path: "/map" },
    { icon: Heart, label: "Favorites", path: "/favorites" },
    { icon: User, label: "Profile", path: "/profile" }
  ];

  const handleLogout = () => {
    logout();
    navigate("/explore");
  };
  
  return (
    <aside className="hidden md:flex md:flex-col w-64 bg-white border-r border-gray-200 h-screen sticky top-0">
      <div className="p-6 border-b border-gray-200">
        <Link to="/explore" className="flex items-center gap-2">
          <div className="bg-gradient-to-br from-orange-500 to-red-500 p-2 rounded-lg">
            <Utensils className="text-white" size={24} />
          </div>
          <span className="text-xl font-bold text-orange-600">RestoRadar</span>
        </Link>
      </div>
      
      <nav className="flex-1 p-4">
        <ul className="space-y-2">
          {navItems.map((item) => {
            const isActive = location.pathname === item.path;
            const Icon = item.icon;
            
            return (
              <li key={item.path}>
                <Link
                  to={item.path}
                  className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                    isActive
                      ? "bg-orange-50 text-orange-600"
                      : "text-gray-700 hover:bg-gray-100"
                  }`}
                >
                  <Icon size={20} />
                  <span>{item.label}</span>
                </Link>
              </li>
            );
          })}
        </ul>
      </nav>

      {/* User Section */}
      <div className="p-4 border-t border-gray-200">
        {isAuthenticated && user ? (
          <div className="space-y-3">
            <div className="px-3 py-2 bg-gray-50 rounded-lg">
              <p className="font-semibold text-sm truncate">{user.name}</p>
              <p className="text-xs text-gray-500 truncate">{user.email}</p>
            </div>
            <Button
              variant="outline"
              className="w-full justify-start"
              onClick={handleLogout}
            >
              <LogOut size={16} className="mr-2" />
              Sign Out
            </Button>
          </div>
        ) : (
          <div className="space-y-2">
            <Link to="/login" className="block">
              <Button variant="default" className="w-full justify-start">
                <LogIn size={16} className="mr-2" />
                Sign In
              </Button>
            </Link>
            <Link to="/register" className="block">
              <Button variant="outline" className="w-full justify-start">
                <UserPlus size={16} className="mr-2" />
                Create Account
              </Button>
            </Link>
          </div>
        )}
      </div>
    </aside>
  );
}