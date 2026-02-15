import { Injectable, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, firstValueFrom } from 'rxjs';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { environment } from '../../env/environment';

import { ChatDTO,MessageDTO,AdminSendMessageRequest,SendMessageRequest } from '../model/ui/chat-dto';

@Injectable({ providedIn: 'root' })
export class SupportChatService {
  private api = `${environment.apiHost}`
  private baseSocekt=`${environment.webS}`

  private stomp?: Client;
  private sub?: StompSubscription;

  private connected$ = new BehaviorSubject<boolean>(false);
  private messages$ = new BehaviorSubject<MessageDTO[]>([]);

  constructor(private http: HttpClient, private zone: NgZone) {}

  connectionStream(): Observable<boolean> { return this.connected$.asObservable(); }
  messagesStream(): Observable<MessageDTO[]> { return this.messages$.asObservable(); }


  setInitialMessages(msgs: MessageDTO[]) {
    this.messages$.next(msgs ?? []);
  }

  async loadMyChat(): Promise<ChatDTO> {
    return firstValueFrom(this.http.get<ChatDTO>(`${this.api}/support/chat`));
  }

  connect(token: string) {
    if (this.stomp?.active) return;

    const socket = new SockJS(`${this.baseSocekt}/ws`); // 
    const client = new Client({
      webSocketFactory: () => socket as any,
      reconnectDelay: 3000,
      connectHeaders: { Authorization: `Bearer ${token}` },
      onConnect: () => {
       this.zone.run(() => this.connected$.next(true));
       this.sub = client.subscribe('/user/queue/support', (m) =>
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
    this.sub = undefined;

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

  private onIncoming(msg: IMessage) {
    const incoming: MessageDTO = JSON.parse(msg.body);
    const current = this.messages$.value;
    this.messages$.next([...current, incoming]);
  }
}