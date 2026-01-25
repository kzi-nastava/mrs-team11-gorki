export interface Passenger {
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
}

export interface Driver {
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
  passengers: Passenger [];
}

export interface ScheduledRide {
  id: number;
  rating: number;
  startTime: string;
  endTime: string;
  startLocation: string;
  destination: string;
  date:Date;
  price: number;
  passengers:Passenger [];
}

export interface PanicRide {
  id: number;
  rating: number;
  startTime: string;
  endTime: string;
  startLocation: string;
  destination: string;
  date:Date;
  price: number;
  passengers:Passenger [];
}

export interface UserHistoryRide {
  id: number;
  rating: number;
  startTime: string;
  endTime: string;
  startLocation: string;
  destination: string;
  date:Date;
  price: number;
  canceled: boolean;
  canceledBy: string;
  cancellationReason: string;
  panic: boolean;
  passengers: Passenger [];
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