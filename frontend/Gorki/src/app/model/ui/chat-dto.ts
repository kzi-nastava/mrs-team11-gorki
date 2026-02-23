export interface MessageDTO {
  sender: string;
  content: string;
  timeStamp: string; // LocalDateTime kao string
}

export interface ChatDTO {
  id: number;
  userId: number;
  adminId: number | null;
  messages: MessageDTO[];
}

export interface SendMessageRequest {
  content: string;
}

export interface AdminSendMessageRequest {
  chatId: number;
  content: string;
}
export interface AdminMessageDTO {
  chatId: number;
  userId: number;
  sender: string;
  content: string;
  timeStamp: string; // ISO
}