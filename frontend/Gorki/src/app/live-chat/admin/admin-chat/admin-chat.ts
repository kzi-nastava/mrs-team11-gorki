import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';

import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../../infrastructure/auth.service';
import { AdminChatService } from '../../../service/admin-chat-service';

import { ChatDTO } from '../../../model/ui/chat-dto';
import { AdminMessageDTO } from '../../../model/ui/chat-dto';

@Component({
  selector: 'app-admin-chat',
   imports: [
    CommonModule,
    ReactiveFormsModule,
    DatePipe,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './admin-chat.html',
  styleUrl: './admin-chat.css',
})
export class AdminChat implements OnInit, OnDestroy {
chats: ChatDTO[] = [];
  selectedChat: ChatDTO | null = null;

  messages: AdminMessageDTO[] = [];

  connected = false;

  msgCtrl = new FormControl('', [Validators.required]);

  private subChats?: Subscription;
  private subMsgs?: Subscription;
  private subConn?: Subscription;

  constructor(
    private auth: AuthService,
    private support: AdminChatService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const token = this.auth.getToken();

    // connect ws
    this.support.connect(token);

    // load inbox
    this.support.loadChats().catch(console.error);

    // streams
    this.subConn = this.support.connectionStream().subscribe(c => {
      Promise.resolve().then(() => {
        this.connected = !!c;
        this.cdr.markForCheck();
      });
    });

    this.subChats = this.support.chatsStream().subscribe(chats => {
      this.chats = chats ?? [];

      // ako je selektovan chat i lista se refreshovala, osvezi referencu
      if (this.selectedChat) {
        const found = this.chats.find(x => x.id === this.selectedChat!.id);
        if (found) this.selectedChat = found;
      }

      this.cdr.markForCheck();
    });

    this.subMsgs = this.support.messagesStream().subscribe(map => {
      if (!this.selectedChat) return;
      this.messages = map[this.selectedChat.id] ?? [];
      this.cdr.markForCheck();
    });
  }

  ngOnDestroy(): void {
    this.subConn?.unsubscribe();
    this.subChats?.unsubscribe();
    this.subMsgs?.unsubscribe();
    this.support.disconnect();
  }

  refresh(): void {
    this.support.loadChats().catch(console.error);
  }

  select(chat: ChatDTO): void {
    this.selectedChat = chat;
    this.support.setHistory(chat);
  }

  send(): void {
    const text = (this.msgCtrl.value || '').trim();
    if (!text || !this.selectedChat) return;

    this.support.send(this.selectedChat.id, text);
    this.msgCtrl.setValue('');
  }

  isAdmin(m: AdminMessageDTO): boolean {
    return (m.sender || '').startsWith('ADMIN:');
  }

  senderLabel(m: AdminMessageDTO): string {
    if (this.isAdmin(m)) return 'ADMIN';
    return 'USER';
  }
}
