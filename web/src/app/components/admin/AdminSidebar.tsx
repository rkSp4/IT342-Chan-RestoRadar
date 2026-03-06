import { NavLink } from "react-router";
import { useAdmin } from "../../contexts/AdminContext";
import { 
  LayoutDashboard, 
  Store, 
  MessageSquare, 
  Users, 
  BarChart3, 
  LogOut,
  ChefHat,
  Home
} from "lucide-react";

export function AdminSidebar() {
  const { logout } = useAdmin();

  const menuItems = [
    { path: "/admin/dashboard", icon: LayoutDashboard, label: "Dashboard" },
    { path: "/admin/restaurants", icon: Store, label: "Restaurants" },
    { path: "/admin/reviews", icon: MessageSquare, label: "Reviews" },
    { path: "/admin/users", icon: Users, label: "Users" },
    { path: "/admin/analytics", icon: BarChart3, label: "Analytics" },
  ];

  return (
    <aside className="hidden md:flex md:flex-col w-64 bg-white border-r border-gray-200">
      <div className="p-6 border-b border-gray-200">
        <div className="flex items-center gap-3">
          <div className="bg-orange-500 p-2 rounded-lg">
            <ChefHat className="size-6 text-white" />
          </div>
          <div>
            <h1 className="font-semibold text-lg">RestoRadar</h1>
            <p className="text-xs text-gray-500">Admin Panel</p>
          </div>
        </div>
      </div>

      <nav className="flex-1 p-4 space-y-1 overflow-y-auto">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                isActive
                  ? "bg-orange-50 text-orange-600"
                  : "text-gray-700 hover:bg-gray-50"
              }`
            }
          >
            <item.icon className="size-5" />
            <span className="font-medium">{item.label}</span>
          </NavLink>
        ))}
      </nav>

      <div className="p-4 border-t border-gray-200 space-y-1">
        <NavLink
          to="/"
          className="flex items-center gap-3 px-4 py-3 w-full text-gray-700 hover:bg-gray-50 rounded-lg transition-colors"
        >
          <Home className="size-5" />
          <span className="font-medium">Back to App</span>
        </NavLink>
        <button
          onClick={logout}
          className="flex items-center gap-3 px-4 py-3 w-full text-gray-700 hover:bg-gray-50 rounded-lg transition-colors"
        >
          <LogOut className="size-5" />
          <span className="font-medium">Logout</span>
        </button>
      </div>
    </aside>
  );
}