import { CreatedRouteDTO } from "./created-route-dto";
import { GetDriverDTO } from "./get-driver-dto";
import { GetPassengerDTO } from "./get-passenger-dto";
import { PriceConfig } from "./price-config";

export interface CreatedRideDTO{
    id: number;
	rideStatus: 'REQUESTED' | 'ACCEPTED' | 'STARTED' | 'FINISHED' | 'CANCELED';
	price:number;
	scheduledTime:string;
	driver: GetDriverDTO;
	route: CreatedRouteDTO;
	priceConfig:PriceConfig;
	linkedPassengers: GetPassengerDTO[];
	creator: GetPassengerDTO;
}