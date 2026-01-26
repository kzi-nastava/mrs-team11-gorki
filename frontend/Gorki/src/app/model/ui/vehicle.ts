export interface Vehicle{
	model:string;
	type:'Standard' | 'Luxury' | 'Van' | '';
	plateNumber:string;
	seats:number;
	babyTransport:boolean;
	petFriendly:boolean;
}