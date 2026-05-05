import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router";

export function OAuthCallbackPage() {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(location.search);

    const accessToken = params.get("accessToken");
    const refreshToken = params.get("refreshToken");
    const id = params.get("id");
    const fullName = params.get("fullName");
    const email = params.get("email");
    const role = params.get("role");

    if (!accessToken || !refreshToken || !id || !fullName || !email || !role) {
      navigate("/login");
      return;
    }

    localStorage.setItem("restoradar_access_token", accessToken);
    localStorage.setItem("restoradar_refresh_token", refreshToken);
    localStorage.setItem(
      "restoradar_user",
      JSON.stringify({
        id,
        fullName,
        email,
        role,
        createdAt: new Date().toISOString(),
      })
    );

    window.location.replace("/home");
  }, [location.search, navigate]);

  return (
    <div className="min-h-screen flex items-center justify-center">
      <p>Signing you in with Google...</p>
    </div>
  );
}