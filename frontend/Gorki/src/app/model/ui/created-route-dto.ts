import { Location } from "./location";

export interface CreatedRouteDTO{
    id:number;
    locations: Location[];
    distance: number;
    estimatedTime: string;
}