export interface GetVehicleDTO{
    id:number;
    model:string;
    type:'STANDARD' | 'LUXURY' | 'VAN';
    plateNumber:string;
    seats:number;
    babyTransport:boolean;
    petFriendly:boolean;
}