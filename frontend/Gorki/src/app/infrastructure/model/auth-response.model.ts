export interface AuthResponse {
  id:number;
  role:string;
  token: string;
  active:boolean;
  blocked:boolean;
  message:string;
}
