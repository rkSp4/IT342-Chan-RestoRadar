import { Link, useNavigate } from "react-router";
import { useState } from "react";
import { Button } from "./ui/button";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "./ui/sheet";
import { Menu, Bell, User, HelpCircle, LogIn, UserPlus, LogOut } from "lucide-react";
import { useAuth } from "../../features/auth/AuthContext";

export function MobileHeader() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    setIsMenuOpen(false);
    navigate("/");
  };

  return (
    <header className="lg:hidden sticky top-0 z-50 bg-white border-b border-gray-200 px-4 py-3">
      <div className="flex items-center justify-between">
        <Link to="/explore" className="flex items-center gap-2">
          <div className="bg-orange-500 p-1.5 rounded-lg">
            <svg className="size-5 text-white" fill="currentColor" viewBox="0 0 20 20">
              <path d="M10 2a6 6 0 00-6 6v3.586l-.707.707A1 1 0 004 14h12a1 1 0 00.707-1.707L16 11.586V8a6 6 0 00-6-6z" />
            </svg>
          </div>
          <span className="font-bold text-lg text-orange-600">RestoRadar</span>
        </Link>

        <div className="flex items-center gap-2">
          <Button variant="ghost" size="icon" className="relative">
            <Bell className="size-5" />
            <span className="absolute top-1 right-1 size-2 bg-red-500 rounded-full"></span>
          </Button>
          
          <Sheet open={isMenuOpen} onOpenChange={setIsMenuOpen}>
            <SheetTrigger asChild>
              <Button variant="ghost" size="icon">
                <Menu className="size-5" />
              </Button>
            </SheetTrigger>
            <SheetContent side="right" className="w-[300px]">
              <SheetHeader>
                <SheetTitle>Menu</SheetTitle>
              </SheetHeader>
              <div className="mt-6 space-y-1">
                {isAuthenticated ? (
                  <>
                    <div className="px-4 py-3 mb-2 border-b border-gray-200">
                      <p className="font-semibold text-sm">{user?.name}</p>
                      <p className="text-xs text-gray-500">{user?.email}</p>
                    </div>
                    <Link 
                      to="/profile" 
                      className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-gray-100"
                      onClick={() => setIsMenuOpen(false)}
                    >
                      <User className="size-5" />
                      <span>Profile</span>
                    </Link>
                    <Link 
                      to="/help" 
                      className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-gray-100"
                      onClick={() => setIsMenuOpen(false)}
                    >
                      <HelpCircle className="size-5" />
                      <span>Help & Guide</span>
                    </Link>
                    <button
                      onClick={handleLogout}
                      className="w-full flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-gray-100 text-red-600"
                    >
                      <LogOut className="size-5" />
                      <span>Sign Out</span>
                    </button>
                  </>
                ) : (
                  <>
                    <Link 
                      to="/login" 
                      className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-gray-100"
                      onClick={() => setIsMenuOpen(false)}
                    >
                      <LogIn className="size-5" />
                      <span>Sign In</span>
                    </Link>
                    <Link 
                      to="/register" 
                      className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-gray-100"
                      onClick={() => setIsMenuOpen(false)}
                    >
                      <UserPlus className="size-5" />
                      <span>Create Account</span>
                    </Link>
                    <Link 
                      to="/help" 
                      className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-gray-100"
                      onClick={() => setIsMenuOpen(false)}
                    >
                      <HelpCircle className="size-5" />
                      <span>Help & Guide</span>
                    </Link>
                  </>
                )}
              </div>
            </SheetContent>
          </Sheet>
        </div>
      </div>
    </header>
  );
}