export interface LocationDTO {
  latitude: number;
  longitude: number;
  address: string;
}

export interface GetRouteDTO {
  id: number;
  locations: LocationDTO[];
  distance: number;
  estimatedTime: number;
}

export interface GetUserDTO {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  address: string;
  profileImage?: string | null;
  active: boolean;
  role: string;
}

export interface GetPassengerDTO {
  user: GetUserDTO;
  favouriteRoutes: GetRouteDTO[];
}

export interface GetRideDTO {
  id: number;
  startingTime: string;    // LocalDateTime / ISO string
  endingTime?: string | null;
  status: string;
  price: number;
  creator?: GetPassengerDTO | null;
  linkedPassengers: GetPassengerDTO[];
  route?: GetRouteDTO | null;
}

export interface GetVehicleDTO {
  id: number;
  model: string;
  type: string;
  plateNumber: string;
  seats: number;
  babyTransport: boolean;
  petFriendly: boolean;
  currentLocation?: LocationDTO | null;
}

export interface GetDriverInfoDTO {
  user: GetUserDTO;
  activityLast24h: number;
  vehicle?: GetVehicleDTO | null;
}

export interface AdminRideMonitorDTO {
  ride: GetRideDTO;
  driver: GetDriverInfoDTO;
  currentLocation?: LocationDTO | null;
}
