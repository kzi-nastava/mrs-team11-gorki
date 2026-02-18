export interface CreatedUserDTO{
    id:number
    email:string;
	firstName:string;
	lastName:string;
	phoneNumber:number;
	address:string;
	profileImage:string | null;
    active:boolean;
    role:string;
}