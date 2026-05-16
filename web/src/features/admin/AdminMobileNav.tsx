import { NavLink } from "react-router";
import { 
  LayoutDashboard, 
  Store, 
  MessageSquare, 
  Users, 
  BarChart3
} from "lucide-react";

export function AdminMobileNav() {
  const menuItems = [
    { path: "/admin/dashboard", icon: LayoutDashboard, label: "Dashboard" },
    { path: "/admin/restaurants", icon: Store, label: "Restaurants" },
    { path: "/admin/reviews", icon: MessageSquare, label: "Reviews" },
    { path: "/admin/users", icon: Users, label: "Users" },
    { path: "/admin/analytics", icon: BarChart3, label: "Analytics" },
  ];

  return (
    <nav className="md:hidden fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 px-2 py-2 z-50">
      <div className="flex items-center justify-around">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `flex flex-col items-center gap-1 px-3 py-2 rounded-lg transition-colors ${
                isActive
                  ? "bg-orange-50 text-orange-600"
                  : "text-gray-600 hover:bg-gray-50"
              }`
            }
          >
            <item.icon className="size-5" />
            <span className="text-xs font-medium">{item.label}</span>
          </NavLink>
        ))}
      </div>
    </nav>
  );
}
