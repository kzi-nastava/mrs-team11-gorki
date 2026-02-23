import { Injectable, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, firstValueFrom } from 'rxjs';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { environment } from '../../env/environment';

import { ChatDTO, MessageDTO,AdminMessageDTO } from '../model/ui/chat-dto';

@Injectable({
  providedIn: 'root',
})
export class AdminChatService {
 
  private api = `${environment.apiHost}/admin/support`;
  private baseSocket = `${environment.webS}`;

  private stomp?: Client;
  private sub?: StompSubscription;

  private connected$ = new BehaviorSubject<boolean>(false);
  private chats$ = new BehaviorSubject<ChatDTO[]>([]);
  private messagesByChat$ = new BehaviorSubject<Record<number, AdminMessageDTO[]>>({});

  constructor(private http: HttpClient, private zone: NgZone) {}

  connectionStream(): Observable<boolean> { return this.connected$.asObservable(); }
  chatsStream(): Observable<ChatDTO[]> { return this.chats$.asObservable(); }
  messagesStream(): Observable<Record<number, AdminMessageDTO[]>> { return this.messagesByChat$.asObservable(); }

  // ===== REST =====

  async loadChats(): Promise<void> {
    const chats = await firstValueFrom(
      this.http.get<ChatDTO[]>(`${this.api}/chats`)
    );

    this.chats$.next(chats ?? []);
  }

  setHistory(chat: ChatDTO) {
    const map = { ...this.messagesByChat$.value };

    const mapped: AdminMessageDTO[] = (chat.messages ?? []).map((m: any) => ({
      chatId: chat.id,
      userId: chat.userId,
      sender: m.sender,
      content: m.content,
      timeStamp: m.timeStamp
    }));

    map[chat.id] = mapped;
    this.messagesByChat$.next(map);
  }

  // ===== WS =====

  connect(token: string) {

    if (this.stomp?.active) return;

    const socket = new SockJS(`${this.baseSocket}/ws`);

    const client = new Client({
      webSocketFactory: () => socket as any,
      reconnectDelay: 3000,
      connectHeaders: { Authorization: `Bearer ${token}` },

      onConnect: () => {

        this.zone.run(() => this.connected$.next(true));

        // ADMIN realtime stream
        this.sub = client.subscribe('/user/queue/support-admin', (m) =>
          this.zone.run(() => this.onIncoming(m))
        );
      },

      onWebSocketClose: () => this.zone.run(() => this.connected$.next(false)),
      onStompError: () => this.zone.run(() => this.connected$.next(false)),
    });

    client.activate();
    this.stomp = client;
  }

  disconnect() {
    this.sub?.unsubscribe();

    if (this.stomp) {
      this.stomp.deactivate();
      this.stomp = undefined;
    }

    this.connected$.next(false);
  }

  send(chatId: number, content: string) {

    if (!this.stomp?.connected) return;

    this.stomp.publish({
      destination: '/app/support.adminSend',
      body: JSON.stringify({ chatId, content }),
    });
  }

  private onIncoming(msg: IMessage) {

    const incoming: AdminMessageDTO = JSON.parse(msg.body);

    const map = { ...this.messagesByChat$.value };

    const list = map[incoming.chatId] ?? [];

    map[incoming.chatId] = [...list, incoming];

    this.messagesByChat$.next(map);
  }
}
