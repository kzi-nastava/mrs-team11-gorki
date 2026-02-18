import { CreatedUserDTO } from "./created-user-dto";
import { CreatedVehicleDTO } from "./created-vehicle-dto";

export interface CreatedDriverDTO{
    user:CreatedUserDTO;
    vehicle:CreatedVehicleDTO;
    status:'ACTIVE' | 'INACTIVE' | 'BUSY';
}