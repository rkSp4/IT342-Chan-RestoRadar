import { Navigate, Outlet } from "react-router";
import { useAdmin } from "../../features/admin/AdminContext";
import { AdminSidebar } from "../../features/admin/AdminSidebar";
import { AdminMobileNav } from "../../features/admin/AdminMobileNav";
import { AdminMobileHeader } from "../../features/admin/AdminMobileHeader";
import { ViewModeIndicator } from "../../features/restaurant/ViewModeIndicator";

export function AdminLayout() {
  const { isAdminAuthenticated } = useAdmin();

  if (!isAdminAuthenticated) {
    return <Navigate to="/admin/login" replace />;
  }

  return (
    <div className="flex h-screen overflow-hidden bg-gray-50">
      <AdminSidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <AdminMobileHeader />
        <main className="flex-1 overflow-y-auto pb-20 md:pb-0">
          <Outlet />
        </main>
      </div>
      <AdminMobileNav />
      <ViewModeIndicator />
    </div>
  );
}