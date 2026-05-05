import { useState } from "react";
import { Card } from "../../shared/components/ui/card";
import { Button } from "../../shared/components/ui/button";
import { Input } from "../../shared/components/ui/input";
import { Badge } from "../../shared/components/ui/badge";
import { 
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../../shared/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "../../shared/components/ui/dialog";
import { Label } from "../../shared/components/ui/label";
import { Textarea } from "../../shared/components/ui/textarea";
import { mockRestaurants } from "../../data/mockData";
import { Restaurant } from "../restaurant/restaurant";
import { Plus, Search, Edit, Trash2, Star, MapPin } from "lucide-react";
import { toast } from "sonner";

export function AdminRestaurantsPage() {
  const [restaurants, setRestaurants] = useState<Restaurant[]>(mockRestaurants);
  const [searchQuery, setSearchQuery] = useState("");
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingRestaurant, setEditingRestaurant] = useState<Restaurant | null>(null);
  const [formData, setFormData] = useState<Partial<Restaurant>>({});

  const filteredRestaurants = restaurants.filter(
    (r) =>
      r.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      r.cuisine.toLowerCase().includes(searchQuery.toLowerCase()) ||
      r.address.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleEdit = (restaurant: Restaurant) => {
    setEditingRestaurant(restaurant);
    setFormData(restaurant);
    setIsDialogOpen(true);
  };

  const handleAdd = () => {
    setEditingRestaurant(null);
    setFormData({});
    setIsDialogOpen(true);
  };

  const handleDelete = (id: string) => {
    setRestaurants(restaurants.filter((r) => r.id !== id));
    toast.success("Restaurant deleted successfully");
  };

  const handleSave = () => {
    if (editingRestaurant) {
      setRestaurants(
        restaurants.map((r) =>
          r.id === editingRestaurant.id ? { ...r, ...formData } : r
        )
      );
      toast.success("Restaurant updated successfully");
    } else {
      const newRestaurant: Restaurant = {
        id: Date.now().toString(),
        name: formData.name || "",
        cuisine: formData.cuisine || "",
        priceRange: formData.priceRange || "$$",
        rating: formData.rating || 0,
        reviewCount: 0,
        address: formData.address || "",
        distance: formData.distance || 0,
        image: formData.image || "",
        hours: formData.hours || "",
        phone: formData.phone || "",
        description: formData.description || "",
        latitude: formData.latitude || 0,
        longitude: formData.longitude || 0,
        tags: formData.tags || [],
      };
      setRestaurants([...restaurants, newRestaurant]);
      toast.success("Restaurant added successfully");
    }
    setIsDialogOpen(false);
  };

  return (
    <div className="p-4 md:p-8 space-y-6">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold">Restaurants</h1>
          <p className="text-gray-500 mt-1">Manage your restaurant listings</p>
        </div>
        <Button onClick={handleAdd} className="gap-2">
          <Plus className="size-4" />
          Add Restaurant
        </Button>
      </div>

      <Card className="p-4">
        <div className="flex items-center gap-2">
          <Search className="size-5 text-gray-400" />
          <Input
            placeholder="Search restaurants..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="border-0 focus-visible:ring-0"
          />
        </div>
      </Card>

      <Card>
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Restaurant</TableHead>
                <TableHead>Cuisine</TableHead>
                <TableHead>Price</TableHead>
                <TableHead>Rating</TableHead>
                <TableHead>Reviews</TableHead>
                <TableHead>Location</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredRestaurants.map((restaurant) => (
                <TableRow key={restaurant.id}>
                  <TableCell>
                    <div className="flex items-center gap-3">
                      <img
                        src={restaurant.image}
                        alt={restaurant.name}
                        className="size-12 rounded-lg object-cover"
                      />
                      <div>
                        <div className="font-medium">{restaurant.name}</div>
                        <div className="text-sm text-gray-500">{restaurant.phone}</div>
                      </div>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge variant="secondary">{restaurant.cuisine}</Badge>
                  </TableCell>
                  <TableCell>{restaurant.priceRange}</TableCell>
                  <TableCell>
                    <div className="flex items-center gap-1">
                      <Star className="size-4 fill-orange-500 text-orange-500" />
                      <span>{restaurant.rating}</span>
                    </div>
                  </TableCell>
                  <TableCell>{restaurant.reviewCount}</TableCell>
                  <TableCell>
                    <div className="flex items-center gap-1 text-sm text-gray-600">
                      <MapPin className="size-4" />
                      {restaurant.distance} mi
                    </div>
                  </TableCell>
                  <TableCell className="text-right">
                    <div className="flex items-center justify-end gap-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleEdit(restaurant)}
                      >
                        <Edit className="size-4" />
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleDelete(restaurant.id)}
                      >
                        <Trash2 className="size-4 text-red-600" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </Card>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>
              {editingRestaurant ? "Edit Restaurant" : "Add New Restaurant"}
            </DialogTitle>
            <DialogDescription>
              {editingRestaurant
                ? "Update restaurant information"
                : "Add a new restaurant to your platform"}
            </DialogDescription>
          </DialogHeader>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="name">Restaurant Name</Label>
              <Input
                id="name"
                value={formData.name || ""}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="cuisine">Cuisine</Label>
              <Input
                id="cuisine"
                value={formData.cuisine || ""}
                onChange={(e) => setFormData({ ...formData, cuisine: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="priceRange">Price Range</Label>
              <Input
                id="priceRange"
                value={formData.priceRange || ""}
                onChange={(e) => setFormData({ ...formData, priceRange: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="phone">Phone</Label>
              <Input
                id="phone"
                value={formData.phone || ""}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
              />
            </div>
            <div className="space-y-2 md:col-span-2">
              <Label htmlFor="address">Address</Label>
              <Input
                id="address"
                value={formData.address || ""}
                onChange={(e) => setFormData({ ...formData, address: e.target.value })}
              />
            </div>
            <div className="space-y-2 md:col-span-2">
              <Label htmlFor="description">Description</Label>
              <Textarea
                id="description"
                value={formData.description || ""}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="hours">Hours</Label>
              <Input
                id="hours"
                value={formData.hours || ""}
                onChange={(e) => setFormData({ ...formData, hours: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="distance">Distance (mi)</Label>
              <Input
                id="distance"
                type="number"
                step="0.1"
                value={formData.distance || ""}
                onChange={(e) => setFormData({ ...formData, distance: parseFloat(e.target.value) })}
              />
            </div>
            <div className="space-y-2 md:col-span-2">
              <Label htmlFor="image">Image URL</Label>
              <Input
                id="image"
                value={formData.image || ""}
                onChange={(e) => setFormData({ ...formData, image: e.target.value })}
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
              Cancel
            </Button>
            <Button onClick={handleSave}>
              {editingRestaurant ? "Update" : "Add"} Restaurant
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
