export interface NotificationDTO {
  id: number;
  purpose: string;     // ili type
  content: string;     // message
  createdAt: string;   // ISO string
  read: boolean;
}
