import { GetUserDTO } from "./get-user-dto";

export interface GetDriverDTO{
    user: GetUserDTO;
    activityLast24h: number;
}