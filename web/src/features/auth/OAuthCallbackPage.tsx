import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router";
import { useAuth } from "./AuthContext";

export function OAuthCallbackPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const { oauthLogin } = useAuth();

  useEffect(() => {
    const params = new URLSearchParams(location.search);

    const accessToken = params.get("accessToken");
    const refreshToken = params.get("refreshToken");
    const id = params.get("id");
    const fullName = params.get("fullName");
    const email = params.get("email");
    const role = params.get("role");

    console.log("OAuth Callback Params:", { accessToken, refreshToken, id, fullName, email, role });

    if (!accessToken || !refreshToken || !id || !fullName || !email || !role) {
      console.error("Missing parameters in OAuth callback URL");
      navigate("/login");
      return;
    }

    oauthLogin(
      {
        id,
        fullName,
        email,
        role,
        createdAt: new Date().toISOString(),
      },
      accessToken,
      refreshToken
    );

    navigate("/explore", { replace: true });
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [location.search]);

  return (
    <div className="min-h-screen flex items-center justify-center">
      <p>Signing you in with Google...</p>
    </div>
  );
}