import { CreatedRouteDTO } from "./created-route-dto";
import { GetDriverDTO } from "./get-driver-dto";
import { GetPassengerDTO } from "./get-passenger-dto";

export interface GetRideDTO{
    id:number;
	rideStatus:string;
	price:number;
	scheduledTime: string;
	startingTime:string;
	endingTime:string;
	panicActivated:boolean;
	cancellationReason:string;
	driver: GetDriverDTO;
	route:CreatedRouteDTO;
	linkedPassengers:GetPassengerDTO[];
	creator: GetPassengerDTO;
}