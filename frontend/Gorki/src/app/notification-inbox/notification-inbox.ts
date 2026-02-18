import { Component, OnDestroy, OnInit, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription, finalize } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { NotificationDTO } from '../model/ui/notification-dto';
import { NotificationService } from '../service/notification-service';
import { AuthService } from '../infrastructure/auth.service';

@Component({
  selector: 'app-notification-inbox',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, MatProgressSpinnerModule],
  templateUrl: './notification-inbox.html',
  styleUrls: ['./notification-inbox.css'],
})
export class NotificationInbox implements OnInit, AfterViewInit, OnDestroy {
  loading = false;

  userId!: number;
  items: NotificationDTO[] = [];
  selected?: NotificationDTO;

  private subLoad?: Subscription;
  private subMark?: Subscription;

  constructor(
    private notificationService: NotificationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.authService.getId();
    if (!id) {
      console.error('No userId found for notifications inbox.');
      return;
    }
    this.userId = Number(id);
  }

  ngAfterViewInit(): void {
    // bitno: tek posle prvog rendera dialoga
    setTimeout(() => this.refresh(), 0);
  }

  ngOnDestroy(): void {
    this.subLoad?.unsubscribe();
    this.subMark?.unsubscribe();
  }

  refresh(): void {
    if (!this.userId) return;

    this.subLoad?.unsubscribe();

    // async boundary da se ne desi promena u istom CD ciklusu
    setTimeout(() => {
      this.loading = true;

      this.subLoad = this.notificationService
        .getAllForUser(this.userId)
        .pipe(finalize(() => (this.loading = false)))
        .subscribe({
          next: (list) => {
            this.items = (list ?? []).slice().sort(
              (a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
            );

            if (this.selected) {
              this.selected =
                this.items.find(x => x.id === this.selected!.id) ?? this.selected;
            }
          },
          error: (err) => console.error('Failed to load notifications:', err),
        });
    }, 0);
  }

  select(n: NotificationDTO): void {
    this.selected = n;

    if (n.read) return;

    // immutable update
    this.items = this.items.map(x => (x.id === n.id ? { ...x, read: true } : x));
    this.selected = { ...n, read: true };

    this.subMark?.unsubscribe();
    this.subMark = this.notificationService.markAsRead(n.id, this.userId).subscribe({
      next: () => {},
      error: (err) => console.error('markAsRead failed:', err),
    });
  }

  trackById(_: number, n: NotificationDTO) {
    return n.id;
  }

  formatTime(iso: string): string {
    try { return new Date(iso).toLocaleString(); } catch { return iso; }
  }
}
