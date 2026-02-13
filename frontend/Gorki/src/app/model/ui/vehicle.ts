export interface Vehicle{
	model:string;
	type:'STANDARD' | 'LUXURY' | 'VAN';
	plateNumber:string;
	seats:number;
	babyTransport:boolean;
	petFriendly:boolean;
}

export interface CurrentLocation{
	latitude:number;
    longitude:number;
    address:string;
}

export interface HomeVehicle{
	id:number,
	currentLocation:CurrentLocation,
	vehicleAvailability:string
}