import { useState, useEffect } from "react";
import { MapContainer, TileLayer, Marker, Popup, useMap } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import { MapPin, Navigation, Loader2 } from "lucide-react";
import { Button } from "../../shared/components/ui/button";
import { StarRating } from "../../features/review/StarRating";
import { Link } from "react-router";
import { useRootContext } from "../../Root";
import { restaurantService, Restaurant } from "../../shared/services/restaurantService";

// Fix Leaflet's broken default icon paths in Vite/webpack builds
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
});

// Custom icons
const userIcon = new L.DivIcon({
  html: `<div style="background:#2563eb;border:3px solid white;border-radius:50%;width:18px;height:18px;box-shadow:0 2px 8px rgba(0,0,0,0.3)"></div>`,
  className: "",
  iconSize: [18, 18],
  iconAnchor: [9, 9],
});

const restaurantIcon = new L.DivIcon({
  html: `<div style="background:#ea580c;border:3px solid white;border-radius:50% 50% 50% 0;width:22px;height:22px;transform:rotate(-45deg);box-shadow:0 2px 8px rgba(0,0,0,0.3)"></div>`,
  className: "",
  iconSize: [22, 22],
  iconAnchor: [11, 22],
});

const favoriteIcon = new L.DivIcon({
  html: `<div style="background:#dc2626;border:3px solid white;border-radius:50% 50% 50% 0;width:22px;height:22px;transform:rotate(-45deg);box-shadow:0 2px 8px rgba(0,0,0,0.3)"></div>`,
  className: "",
  iconSize: [22, 22],
  iconAnchor: [11, 22],
});

// Helper component to fly to user location
function FlyToLocation({ coords }: { coords: [number, number] }) {
  const map = useMap();
  useEffect(() => {
    map.flyTo(coords, 15, { duration: 1.5 });
  }, [coords, map]);
  return null;
}

export function MapPage() {
  const { favorites } = useRootContext();
  const [userLocation, setUserLocation] = useState<[number, number] | null>(null);
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [selected, setSelected] = useState<Restaurant | null>(null);
  const [locating, setLocating] = useState(false);
  const [locationError, setLocationError] = useState<string | null>(null);

  // Default center: Manila, Philippines (matches your project context)
  const defaultCenter: [number, number] = [14.5995, 120.9842];

  const locateUser = () => {
    if (!navigator.geolocation) {
      setLocationError("Geolocation is not supported by your browser.");
      return;
    }
    setLocating(true);
    setLocationError(null);
    navigator.geolocation.getCurrentPosition(
      async (pos) => {
        const coords: [number, number] = [pos.coords.latitude, pos.coords.longitude];
        setUserLocation(coords);
        setLocating(false);
        try {
          const result = await restaurantService.getNearby(
            pos.coords.latitude,
            pos.coords.longitude,
            5
          );
          setRestaurants(result.restaurants);
        } catch {
          // Fall back to all restaurants if nearby fails
          const result = await restaurantService.getAll();
          setRestaurants(result.restaurants);
        }
      },
      () => {
        setLocating(false);
        setLocationError("Could not get your location. Please allow location access.");
        // Still load all restaurants
        restaurantService.getAll().then((r) => setRestaurants(r.restaurants)).catch(() => {});
      }
    );
  };

  // Load restaurants on mount, prompt for location
  useEffect(() => {
    restaurantService.getAll().then((r) => setRestaurants(r.restaurants)).catch(() => {});
    locateUser();
  }, []);

  return (
    <div className="h-screen flex flex-col">
      {/* Mobile header */}
      <div className="md:hidden bg-white border-b border-gray-200 p-4 z-10">
        <h1 className="text-xl font-bold">Map View</h1>
        <p className="text-sm text-gray-500">Tap markers to see restaurant details</p>
      </div>

      <div className="flex-1 relative">
        <MapContainer
          center={userLocation ?? defaultCenter}
          zoom={14}
          className="w-full h-full"
          zoomControl={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />

          {userLocation && (
            <>
              <FlyToLocation coords={userLocation} />
              <Marker position={userLocation} icon={userIcon}>
                <Popup>You are here</Popup>
              </Marker>
            </>
          )}

          {restaurants.map((r) => (
            <Marker
              key={r.id}
              position={[r.latitude, r.longitude]}
              icon={favorites.includes(r.id) ? favoriteIcon : restaurantIcon}
              eventHandlers={{ click: () => setSelected(r) }}
            />
          ))}
        </MapContainer>

        {/* Locate me button */}
        <div className="absolute top-4 right-4 z-[1000] flex flex-col gap-2">
          <Button
            size="icon"
            variant="secondary"
            className="bg-white shadow-md"
            onClick={locateUser}
            disabled={locating}
            title="Find my location"
          >
            {locating ? (
              <Loader2 size={18} className="animate-spin" />
            ) : (
              <Navigation size={18} />
            )}
          </Button>
        </div>

        {/* Location error */}
        {locationError && (
          <div className="absolute top-4 left-4 right-16 z-[1000] bg-red-50 border border-red-200 text-red-700 text-sm px-3 py-2 rounded-lg">
            {locationError}
          </div>
        )}

        {/* Selected restaurant info card */}
        {selected && (
          <div className="absolute bottom-4 left-4 right-4 md:left-6 md:right-auto md:w-96 z-[1000]">
            <div className="bg-white rounded-xl shadow-xl p-4">
              <button
                onClick={() => setSelected(null)}
                className="absolute top-3 right-3 text-gray-400 hover:text-gray-600 text-lg leading-none"
              >
                ×
              </button>
              <div className="flex items-start gap-3">
                <div className="w-16 h-16 rounded-lg bg-orange-100 flex items-center justify-center flex-shrink-0 overflow-hidden">
                  {selected.photos ? (
                    <img src={selected.photos} alt={selected.name} className="w-full h-full object-cover" />
                  ) : (
                    <MapPin size={24} className="text-orange-400" />
                  )}
                </div>
                <div className="flex-1 min-w-0">
                  <h3 className="font-bold text-base mb-0.5 truncate">{selected.name}</h3>
                  <p className="text-sm text-gray-500 mb-1">
                    {[selected.cuisineType, selected.priceRange].filter(Boolean).join(" • ")}
                  </p>
                  <div className="flex items-center gap-2 mb-2">
                    <StarRating rating={selected.averageRating} size={14} />
                    <span className="text-xs text-gray-500">({selected.reviewCount})</span>
                  </div>
                  {selected.distance !== undefined && (
                    <p className="text-xs text-gray-500 mb-2">{selected.distance} km away</p>
                  )}
                  <div className="flex gap-2">
                    <Link to={`/restaurant/${selected.id}`} className="flex-1">
                      <Button size="sm" className="w-full">View Details</Button>
                    </Link>
                    <a
                      href={`https://www.google.com/maps/search/?api=1&query=${selected.latitude},${selected.longitude}`}
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      <Button size="sm" variant="outline">Directions</Button>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}