import { CreatedRouteDTO } from "./created-route-dto";
import { GetUserDTO } from "./get-user-dto";

export interface GetPassengerDTO{
    user: GetUserDTO;
    favouriteRoutes: CreatedRouteDTO[];
}