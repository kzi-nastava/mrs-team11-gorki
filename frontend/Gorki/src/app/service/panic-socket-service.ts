import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export interface PanicEventDTO {
  rideId: number;
  triggeredBy?: string; // DRIVER/PASSENGER
  time: string;
}

@Injectable({ providedIn: 'root' })
export class PanicSocketService {
  private client?: Client;

  connect(token: string, onEvent: (e: PanicEventDTO) => void) {
    if (this.client?.active) return;

    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 3000,
      connectHeaders: { Authorization: `Bearer ${token}` },
      onConnect: () => {
        this.client?.subscribe('/topic/panic', (msg: IMessage) => {
          onEvent(JSON.parse(msg.body));
        });
      },
    });

    this.client.activate();
  }

  disconnect() {
    this.client?.deactivate();
    this.client = undefined;
  }
}