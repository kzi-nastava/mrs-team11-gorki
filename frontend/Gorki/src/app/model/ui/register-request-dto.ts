export interface RegisterRequestDTO {
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
  address: string;
  phoneNumber: number;
  profileImage?: string | null;
}