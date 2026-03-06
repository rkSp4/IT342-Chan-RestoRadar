import { useState } from "react";
import { Card } from "../../components/ui/card";
import { Button } from "../../components/ui/button";
import { Input } from "../../components/ui/input";
import { Badge } from "../../components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../../components/ui/table";
import { Search, Ban, CheckCircle } from "lucide-react";
import { toast } from "sonner";

interface User {
  id: string;
  name: string;
  email: string;
  avatar: string;
  joinDate: string;
  reviewCount: number;
  favoriteCount: number;
  status: "active" | "banned";
}

export function AdminUsersPage() {
  const [users, setUsers] = useState<User[]>([
    {
      id: "1",
      name: "Sarah Johnson",
      email: "sarah@example.com",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah",
      joinDate: "2026-01-15",
      reviewCount: 12,
      favoriteCount: 8,
      status: "active",
    },
    {
      id: "2",
      name: "Mike Chen",
      email: "mike@example.com",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Mike",
      joinDate: "2026-01-20",
      reviewCount: 8,
      favoriteCount: 15,
      status: "active",
    },
    {
      id: "3",
      name: "Emily Rodriguez",
      email: "emily@example.com",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Emily",
      joinDate: "2026-02-01",
      reviewCount: 15,
      favoriteCount: 12,
      status: "active",
    },
    {
      id: "4",
      name: "David Park",
      email: "david@example.com",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=David",
      joinDate: "2026-02-10",
      reviewCount: 6,
      favoriteCount: 10,
      status: "active",
    },
    {
      id: "5",
      name: "Maria Garcia",
      email: "maria@example.com",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Maria",
      joinDate: "2026-02-15",
      reviewCount: 20,
      favoriteCount: 18,
      status: "active",
    },
  ]);
  const [searchQuery, setSearchQuery] = useState("");

  const filteredUsers = users.filter(
    (user) =>
      user.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleToggleStatus = (id: string) => {
    setUsers(
      users.map((user) =>
        user.id === id
          ? {
              ...user,
              status: user.status === "active" ? "banned" : "active",
            }
          : user
      )
    );
    const user = users.find((u) => u.id === id);
    toast.success(
      `User ${user?.status === "active" ? "banned" : "activated"} successfully`
    );
  };

  return (
    <div className="p-4 md:p-8 space-y-6">
      <div>
        <h1 className="text-3xl font-bold">Users</h1>
        <p className="text-gray-500 mt-1">Manage platform users</p>
      </div>

      <Card className="p-4">
        <div className="flex items-center gap-2">
          <Search className="size-5 text-gray-400" />
          <Input
            placeholder="Search users..."
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
                <TableHead>User</TableHead>
                <TableHead>Email</TableHead>
                <TableHead>Join Date</TableHead>
                <TableHead>Reviews</TableHead>
                <TableHead>Favorites</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredUsers.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>
                    <div className="flex items-center gap-3">
                      <img
                        src={user.avatar}
                        alt={user.name}
                        className="size-10 rounded-full"
                      />
                      <span className="font-medium">{user.name}</span>
                    </div>
                  </TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>
                    {new Date(user.joinDate).toLocaleDateString()}
                  </TableCell>
                  <TableCell>{user.reviewCount}</TableCell>
                  <TableCell>{user.favoriteCount}</TableCell>
                  <TableCell>
                    <Badge
                      variant={user.status === "active" ? "default" : "destructive"}
                    >
                      {user.status}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-right">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleToggleStatus(user.id)}
                    >
                      {user.status === "active" ? (
                        <Ban className="size-4 text-red-600" />
                      ) : (
                        <CheckCircle className="size-4 text-green-600" />
                      )}
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </Card>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="p-6">
          <p className="text-sm text-gray-600">Total Users</p>
          <p className="text-2xl font-bold mt-1">{users.length}</p>
          <p className="text-xs text-green-600 mt-2">+8 this week</p>
        </Card>

        <Card className="p-6">
          <p className="text-sm text-gray-600">Active Users</p>
          <p className="text-2xl font-bold mt-1">
            {users.filter((u) => u.status === "active").length}
          </p>
          <p className="text-xs text-gray-500 mt-2">Currently active</p>
        </Card>

        <Card className="p-6">
          <p className="text-sm text-gray-600">Total Reviews</p>
          <p className="text-2xl font-bold mt-1">
            {users.reduce((acc, u) => acc + u.reviewCount, 0)}
          </p>
          <p className="text-xs text-gray-500 mt-2">All time</p>
        </Card>

        <Card className="p-6">
          <p className="text-sm text-gray-600">Avg Reviews/User</p>
          <p className="text-2xl font-bold mt-1">
            {(users.reduce((acc, u) => acc + u.reviewCount, 0) / users.length).toFixed(1)}
          </p>
          <p className="text-xs text-gray-500 mt-2">Per user</p>
        </Card>
      </div>
    </div>
  );
}
