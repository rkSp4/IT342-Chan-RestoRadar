import { useNavigate, useLocation } from "react-router";
import { useAuth } from "../contexts/AuthContext";
import { Button } from "../components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { Utensils, LogOut } from "lucide-react";

export function HomePage() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const { justRegistered, name } = (location.state as { justRegistered?: boolean; name?: string }) || {};

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-orange-50 via-red-50 to-orange-50 p-4">
      <div className="w-full max-w-md">
        <Card>
          <CardHeader className="space-y-1 text-center">
            <div className="flex justify-center mb-4">
              <div className="bg-gradient-to-br from-orange-500 to-red-500 p-4 rounded-2xl shadow-lg">
                <Utensils className="size-8 text-white" />
              </div>
            </div>
            <CardTitle className="text-2xl">Welcome to RestoRadar</CardTitle>
            <CardDescription>
              {justRegistered 
                ? `You have registered, ${name || user?.fullName}!`
                : `You are logged in as ${user?.fullName || user?.email}`
              }
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="bg-green-50 border border-green-200 rounded-md p-4 text-center">
              <p className="text-green-800 font-medium">
                {justRegistered ? "Account created successfully!" : "Successfully authenticated!"}
              </p>
              <p className="text-green-600 text-sm mt-1">{user?.email}</p>
            </div>
            <Button 
              onClick={handleLogout}
              variant="outline"
              className="w-full"
            >
              <LogOut className="size-4 mr-2" />
              Sign Out
            </Button>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
