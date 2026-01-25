export interface Passenger {
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
}

export interface Ride {
  id: number;
  rating: number;
  startTime: string;
  endTime: string;
  startLocation: string;
  destination: string;
  date:Date;
  price: number;
  canceled: boolean;
  panic: boolean;
  passengers:Passenger []; 
}

export interface DriverScheduledRide {
  id: number;
  rating: number;
  startTime: string;
  startLocation: string;
  destination: string;
  date:Date;
  price: number;
  canceled: boolean;
  cancelationReason:string;
  panic: boolean;
  passengers:Passenger []; 
}