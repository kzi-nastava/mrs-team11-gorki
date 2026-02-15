import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

import { AuthService } from '../../../infrastructure/auth.service';
import { SupportChatService } from '../../../service/chat-service';
import { MessageDTO } from '../../../model/ui/chat-dto';

@Component({
  selector: 'app-chat-dialog',
  templateUrl: './chat-dialog.html',
  styleUrls: ['./chat-dialog.css'],
  standalone: true,
  imports: [
    MatIconModule,
    DatePipe,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    CommonModule,
  ],
})
export class ChatDialog implements OnInit, OnDestroy {
  msgCtrl = new FormControl('', [Validators.required]);

  messages: MessageDTO[] = [];
  connected = false;

  private subM?: Subscription;
  private subC?: Subscription;

  constructor(
    private dialogRef: MatDialogRef<ChatDialog>,
    private auth: AuthService,
    private support: SupportChatService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // prvo subscribe (da uhvati i initial i kasnije poruke)
    this.subM = this.support.messagesStream().subscribe((m) => {
      this.messages = m ?? [];
      this.cdr.markForCheck();
    });

    this.subC = this.support.connectionStream().subscribe((c) => {
      // odlozi promenu za sledeci microtask
      Promise.resolve().then(() => {
        this.connected = !!c;
        this.cdr.markForCheck();
      });
    });

    // ucitaj istoriju pa tek onda connect
    this.support
      .loadMyChat()
      .then((chat) => {
        this.support.setInitialMessages(chat?.messages ?? []);
        this.cdr.markForCheck();

        const token = this.auth.getToken();
        this.support.connect(token);
      })
      .catch((err) => {
        console.error('Failed to load chat history:', err);

        // i dalje pokusaj connect, da chat radi i bez istorije
        const token = this.auth.getToken();
        this.support.connect(token);
      });
  }

  ngOnDestroy(): void {
    this.subM?.unsubscribe();
    this.subC?.unsubscribe();
    this.support.disconnect();
  }

  close(): void {
    this.dialogRef.close();
  }


  send(): void {
  const text = (this.msgCtrl.value || '').trim();
  if (!text) return;

  // 1) optimistic: prikaži odmah
  const optimistic: MessageDTO = {
    content: text,
    sender: 'ME at '+(new Date().getMonth()+1)+ '/'+new Date().getDate()+'/'+ new Date().getFullYear(),                
    sentAt: new Date().toISOString()
  } as any;

  this.messages = [...this.messages, optimistic];

  // 2) pošalji serveru
  this.support.send(text);

  // 3) očisti input
  this.msgCtrl.setValue('');
}


  isAdminMessage(m: MessageDTO): boolean {
    return (m?.sender || '').startsWith('ADMIN:');
  }
}
