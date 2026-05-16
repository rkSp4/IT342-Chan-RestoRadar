import { useAdmin } from "./AdminContext";
import { useNavigate } from "react-router";
import { Button } from "../../shared/components/ui/button";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "../../shared/components/ui/sheet";
import { Menu, LogOut, ChefHat, Home } from "lucide-react";
import { useState } from "react";
import { Link } from "react-router";

export function AdminMobileHeader() {
  const { logout } = useAdmin();
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/admin/login");
    setIsMenuOpen(false);
  };

  return (
    <header className="md:hidden sticky top-0 z-40 bg-white border-b border-gray-200 px-4 py-3">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="bg-orange-500 p-1.5 rounded-lg">
            <ChefHat className="size-5 text-white" />
          </div>
          <div>
            <span className="font-bold text-sm text-orange-600">FoodSpot</span>
            <p className="text-xs text-gray-500">Admin</p>
          </div>
        </div>

        <Sheet open={isMenuOpen} onOpenChange={setIsMenuOpen}>
          <SheetTrigger asChild>
            <Button variant="ghost" size="icon">
              <Menu className="size-5" />
            </Button>
          </SheetTrigger>
          <SheetContent side="right" className="w-[300px]">
            <SheetHeader>
              <SheetTitle>Admin Menu</SheetTitle>
            </SheetHeader>
            <div className="mt-6 space-y-1">
              <Link
                to="/"
                className="flex items-center gap-3 px-4 py-3 w-full rounded-lg hover:bg-gray-100"
                onClick={() => setIsMenuOpen(false)}
              >
                <Home className="size-5" />
                <span>Back to App</span>
              </Link>
              <button
                onClick={handleLogout}
                className="flex items-center gap-3 px-4 py-3 w-full rounded-lg hover:bg-gray-100 text-red-600"
              >
                <LogOut className="size-5" />
                <span>Logout</span>
              </button>
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </header>
  );
}