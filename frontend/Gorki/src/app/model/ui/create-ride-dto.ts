import { CreateRouteDTO } from "./create-route-dto";

export interface CreateRideDTO{
    scheduledTime: string;
	route:CreateRouteDTO;
	linkedPassengersEmails: string[];
	creatorId: number;
	babyTransport: boolean;
    petFriendly: boolean;
    vehicleType: 'STANDARD' | 'LUXURY' | 'VAN';
}