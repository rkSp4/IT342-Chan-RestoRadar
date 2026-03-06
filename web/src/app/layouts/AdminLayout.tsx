import { Navigate, Outlet } from "react-router";
import { useAdmin } from "../contexts/AdminContext";
import { AdminSidebar } from "../components/admin/AdminSidebar";
import { AdminMobileNav } from "../components/admin/AdminMobileNav";
import { AdminMobileHeader } from "../components/admin/AdminMobileHeader";
import { ViewModeIndicator } from "../components/ViewModeIndicator";

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