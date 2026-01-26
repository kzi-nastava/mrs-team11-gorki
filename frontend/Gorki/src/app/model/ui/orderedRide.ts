import { Route } from "./route";
import { User } from "./user";

export interface OrderedRide{
    route: Route;
    creator: User;
    linkedPassengers: User[];
    scheduledTime: string;
    price: number;
}