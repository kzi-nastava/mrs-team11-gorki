export interface CreateUserDTO{
    email:string;
	password:string;
	firstName:string;
	lastName:string;
	phoneNumber:number;
	address:string;
	profileImage:string | null;
}