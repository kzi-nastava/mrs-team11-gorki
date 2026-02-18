import { LocationDTO } from "./location-dto";

export interface GetRouteDTO{
    id:number;
    locations: LocationDTO[];
    distance: number;
    estimatedTime: string;
}