import { CreateUserDTO } from "./create-user-dto";
import { CreateVehicleDTO } from "./create-vehicle-dto";

export interface CreateDriverDTO{
    user:CreateUserDTO;
    vehicle:CreateVehicleDTO;
}