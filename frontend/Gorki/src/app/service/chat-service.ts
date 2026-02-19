import { Injectable, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, firstValueFrom } from 'rxjs';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { environment } from '../../env/environment';

import { ChatDTO, MessageDTO } from '../model/ui/chat-dto';

@Injectable({ providedIn: 'root' })
export class SupportChatService {
  private api = `${environment.apiHost}`;
  private baseSocekt = `${environment.webS}`;

  private stomp?: Client;
  private subChat?: StompSubscription;

  private subNotif?: StompSubscription;

  private connected$ = new BehaviorSubject<boolean>(false);
  private messages$ = new BehaviorSubject<MessageDTO[]>([]);
  private notifications$ = new BehaviorSubject<any | null>(null);

  constructor(private http: HttpClient, private zone: NgZone) {}

  connectionStream(): Observable<boolean> {
    return this.connected$.asObservable();
  }

  messagesStream(): Observable<MessageDTO[]> {
    return this.messages$.asObservable();
  }

  notificationStream(): Observable<any | null> {
    return this.notifications$.asObservable();
  }

  setInitialMessages(msgs: MessageDTO[]) {
    this.messages$.next(msgs ?? []);
  }

  async loadMyChat(): Promise<ChatDTO> {
    return firstValueFrom(this.http.get<ChatDTO>(`${this.api}/support/chat`));
  }

  connect(token: string) {
    if (this.stomp?.active) return;

    const socket = new SockJS(`${this.baseSocekt}/ws`);

    const client = new Client({
      webSocketFactory: () => socket as any,
      reconnectDelay: 3000,
      connectHeaders: { Authorization: `Bearer ${token}` },

      onConnect: () => {
        console.log('WS CONNECTED (SupportChatService)');
        this.zone.run(() => this.connected$.next(true));
        this.subChat = client.subscribe('/user/queue/support', (m) =>
          this.zone.run(() => this.onIncomingChat(m))
        );
        this.subNotif = client.subscribe('/user/queue/notifications', (m) =>
          this.zone.run(() => this.onIncomingNotification(m))
        );
      },

      onWebSocketClose: () => this.zone.run(() => this.connected$.next(false)),
      onStompError: () => this.zone.run(() => this.connected$.next(false)),
    });

    client.activate();
    this.stomp = client;
  }

  disconnect() {
    this.subChat?.unsubscribe();
    this.subChat = undefined;

    this.subNotif?.unsubscribe();
    this.subNotif = undefined;

    if (this.stomp) {
      this.stomp.deactivate();
      this.stomp = undefined;
    }

    this.connected$.next(false);
  }

  send(content: string) {
    if (!this.stomp?.connected) return;

    this.stomp.publish({
      destination: '/app/support.send',
      body: JSON.stringify({ content }),
    });
  }

  private onIncomingChat(msg: IMessage) {
    console.log('NOTIF RAW:', msg.body);
    const incoming: MessageDTO = JSON.parse(msg.body);
    const current = this.messages$.value;
    this.messages$.next([...current, incoming]);
  }

  private onIncomingNotification(msg: IMessage) {
    const notification = JSON.parse(msg.body);
    this.notifications$.next(notification);
  }
}
