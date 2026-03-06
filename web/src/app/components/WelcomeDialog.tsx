import { useState, useEffect } from "react";
import { Button } from "../components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "../components/ui/dialog";
import { ChefHat, Shield, Smartphone, Monitor } from "lucide-react";
import { Link } from "react-router";

export function WelcomeDialog() {
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    const hasSeenWelcome = localStorage.getItem("hasSeenWelcome");
    if (!hasSeenWelcome) {
      setIsOpen(true);
    }
  }, []);

  const handleClose = () => {
    localStorage.setItem("hasSeenWelcome", "true");
    setIsOpen(false);
  };

  return (
    <Dialog open={isOpen} onOpenChange={handleClose}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-3 text-2xl">
            <div className="bg-orange-500 p-2 rounded-lg">
              <ChefHat className="size-6 text-white" />
            </div>
            Welcome to FoodSpot!
          </DialogTitle>
          <DialogDescription className="text-base">
            Your complete food discovery and management platform
          </DialogDescription>
        </DialogHeader>
        
        <div className="space-y-6 py-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="bg-gradient-to-br from-orange-50 to-red-50 p-6 rounded-lg">
              <div className="flex items-center gap-3 mb-3">
                <Monitor className="size-6 text-orange-600" />
                <h3 className="font-semibold text-lg">User Experience</h3>
              </div>
              <ul className="space-y-2 text-sm text-gray-700">
                <li>• Discover restaurants near you</li>
                <li>• Read and write reviews</li>
                <li>• Save your favorites</li>
                <li>• View on interactive map</li>
                <li>• Responsive design for all devices</li>
              </ul>
            </div>

            <div className="bg-gradient-to-br from-blue-50 to-indigo-50 p-6 rounded-lg">
              <div className="flex items-center gap-3 mb-3">
                <Shield className="size-6 text-blue-600" />
                <h3 className="font-semibold text-lg">Admin Dashboard</h3>
              </div>
              <ul className="space-y-2 text-sm text-gray-700">
                <li>• Manage restaurants</li>
                <li>• Moderate reviews</li>
                <li>• View analytics</li>
                <li>• Manage users</li>
                <li>• Mobile-optimized admin panel</li>
              </ul>
              <div className="mt-4 p-3 bg-white rounded border border-blue-200">
                <p className="text-xs font-semibold text-blue-900 mb-1">Demo Admin Access:</p>
                <p className="text-xs text-gray-600">Email: admin@foodspot.com</p>
                <p className="text-xs text-gray-600">Password: admin123</p>
              </div>
            </div>
          </div>

          <div className="bg-gray-50 p-4 rounded-lg">
            <div className="flex items-start gap-3">
              <Smartphone className="size-5 text-gray-600 mt-0.5" />
              <div>
                <h4 className="font-semibold mb-1">Mobile Optimized</h4>
                <p className="text-sm text-gray-600">
                  Both the user app and admin dashboard work seamlessly on mobile devices with 
                  responsive navigation and touch-optimized interfaces.
                </p>
              </div>
            </div>
          </div>
        </div>

        <DialogFooter className="flex-col sm:flex-row gap-2">
          <Link to="/admin/login" onClick={handleClose} className="w-full sm:w-auto">
            <Button variant="outline" className="w-full">
              <Shield className="size-4 mr-2" />
              Go to Admin
            </Button>
          </Link>
          <Button onClick={handleClose} className="w-full sm:w-auto">
            Start Exploring
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
