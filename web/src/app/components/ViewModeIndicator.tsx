import { useState, useEffect } from "react";
import { Smartphone, Monitor } from "lucide-react";

export function ViewModeIndicator() {
  const [isMobile, setIsMobile] = useState(false);

  useEffect(() => {
    const checkMobile = () => {
      setIsMobile(window.innerWidth < 768);
    };
    
    checkMobile();
    window.addEventListener("resize", checkMobile);
    
    return () => window.removeEventListener("resize", checkMobile);
  }, []);

  return (
    <div className="fixed bottom-24 right-4 md:bottom-4 bg-white shadow-lg rounded-full p-2 border border-gray-200 z-40">
      {isMobile ? (
        <div className="flex items-center gap-2 px-3 py-1">
          <Smartphone className="size-4 text-blue-600" />
          <span className="text-xs font-medium text-gray-700">Mobile View</span>
        </div>
      ) : (
        <div className="flex items-center gap-2 px-3 py-1">
          <Monitor className="size-4 text-green-600" />
          <span className="text-xs font-medium text-gray-700">Desktop View</span>
        </div>
      )}
    </div>
  );
}
