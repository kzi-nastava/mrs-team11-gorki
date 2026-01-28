export interface RideInProgressDTO {
  rideId: number;
  route: {
    locations: {
      latitude: number;
      longitude: number;
      address: string;
    }[];
  };
}
