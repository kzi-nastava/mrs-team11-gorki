import { Passenger } from "../../rides/models/ride";

export interface DriverHistoryRide {
  rideId: number;
  startTime: string; // ISO string
  endTime: string;   // ISO string
  startingPoint:string;
  endingPoint:string;
  canceled: boolean;
  canceledBy: string | null;
  price: number;
  panicActivated: boolean;
  passengers: Passenger[];
}
