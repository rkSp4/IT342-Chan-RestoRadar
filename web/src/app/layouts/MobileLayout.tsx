import { useState, useEffect } from "react";
import { Outlet } from "react-router";
import { useRootContext } from "../Root";
import { Button } from "../components/ui/button";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "../components/ui/sheet";
import { Menu, X } from "lucide-react";

export function MobileLayout() {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const context = useRootContext();

  return (
    <div className="flex flex-col h-screen">
      {/* Mobile Header */}
      <header className="flex items-center justify-between p-4 bg-white border-b border-gray-200 md:hidden">
        <h1 className="text-xl font-bold text-orange-600">FoodSpot</h1>
        <Sheet open={isMobileMenuOpen} onOpenChange={setIsMobileMenuOpen}>
          <SheetTrigger asChild>
            <Button variant="ghost" size="sm">
              <Menu className="size-6" />
            </Button>
          </SheetTrigger>
          <SheetContent side="right">
            <SheetHeader>
              <SheetTitle>Menu</SheetTitle>
            </SheetHeader>
            {/* Mobile menu content can be added here */}
          </SheetContent>
        </Sheet>
      </header>

      {/* Content */}
      <main className="flex-1 overflow-y-auto">
        <Outlet context={context} />
      </main>
    </div>
  );
}
