export interface GetUserDTO{
    id:number;
	email:string;
	firstName:string;
	lastName:string;
	phoneNumber:number;
	address:string;
	profileImage:string;
    active:boolean;
    blocked:boolean;
	blockReason:string;
	role:string;
}