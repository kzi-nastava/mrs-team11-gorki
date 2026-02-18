import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { Login } from '../../login/login';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Subscription } from 'rxjs';

import { AuthService } from '../../infrastructure/auth.service';
import { MapService } from '../../map/map-service';
import { ChatDialog } from '../../live-chat/users/chat-dialog/chat-dialog';
import { AdminChat } from '../../live-chat/admin/admin-chat/admin-chat';
import { RatingPanel } from '../../rides/rating-panel/rating-panel';
import { RatingService } from '../../service/rating-service';
import { SupportChatService } from '../../service/chat-service';
import { CreateRatingDto } from '../../model/ui/create-rating-dto';
import { RatingConfirmPanel } from '../../rides/rating-confirm-panel/rating-confirm-panel';
import { NotificationInbox } from '../../notification-inbox/notification-inbox';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    MatMenuModule,
    MatIconModule,
    RouterOutlet,
    RouterLink,
    Login,
    MatButtonToggleModule,
    RatingPanel,
  ],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css'],
})
export class Navbar implements OnInit, OnDestroy {
  isRegistrationOpen = false;
  isLoginOpen = false;
  isActive = true;

  isLoggedIn = false;
  role = '';

  private subAuth?: Subscription;
  private subWs?: Subscription;

  // rating modal state
   ratingDialogOpen = false;
  private ratingDialogRef?: MatDialogRef<RatingPanel>;
  private ratingRideId: number | null = null;

  // confirm modal state (mali modal)
  private confirmOpen = false;
  private confirmRef?: MatDialogRef<RatingConfirmPanel>;

  constructor(
    private authService: AuthService,
    private router: Router,
    private mapService: MapService,
    private dialog: MatDialog,
    private ratingService: RatingService,
    private supportChatService: SupportChatService
  ) {}

  ngOnInit(): void {
    // inicijalno stanje
    this.refreshAuthState();
    this.ensureWsConnectedIfLoggedIn();

    if (this.isLoggedIn && this.role === 'PASSENGER') {
      this.checkPendingLatestAndOpenFlow();
    }

    this.subAuth = this.authService.userState.subscribe(() => {
      this.refreshAuthState();
      this.ensureWsConnectedIfLoggedIn();

      if (this.isLoggedIn && this.role === 'PASSENGER') {
        this.checkPendingLatestAndOpenFlow();
      } else {
        this.closeAllRatingUi();
      }
    });

    this.subWs = this.supportChatService.notificationStream().subscribe((n) => {
      if (!n) return;
      if (!this.isLoggedIn || this.role !== 'PASSENGER') return;

      if (n.type === 'RATING_AVAILABLE') {
        const idNum = n.rideId != null ? Number(n.rideId) : null;
        if (idNum && !Number.isNaN(idNum)) {
          this.openRateFlow(idNum); 
        }
      }
    });
  }

  ngOnDestroy(): void {
    this.subAuth?.unsubscribe();
    this.subWs?.unsubscribe();
  }

  private refreshAuthState(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.role = this.authService.getRole();
  }

  private ensureWsConnectedIfLoggedIn(): void {
    if (!this.isLoggedIn) return;
    const token = this.authService.getToken();
    if (!token) return;
    this.supportChatService.connect(token);
  }

  private checkPendingLatestAndOpenFlow(): void {
    const token = this.authService.getToken();
    if (!token) return;

    this.ratingService.getPendingLatest().subscribe({
      next: (rideId) => {
        const idNum = rideId != null ? Number(rideId) : null;
        if (idNum && !Number.isNaN(idNum)) {
          this.openRateFlow(idNum);
        }
      },
      error: (err) => console.error('pending-latest ERROR', err),
    });
  }


  private openRateFlow(rideId: number): void {
    if (this.ratingDialogOpen) return;
    if (this.confirmOpen) return;

    this.confirmOpen = true;

    this.confirmRef = this.dialog.open(RatingConfirmPanel, {
      width: '420px',
      maxWidth: '92vw',
      autoFocus: false,
      disableClose: true,
      data: {
        title: 'Ride completed',
        message: 'Your ride was completed successfully. Would you like to rate it now?',
      },
    });

    this.confirmRef.afterClosed().subscribe((wantRate: boolean) => {
      this.confirmOpen = false;
      this.confirmRef = undefined;

      if (wantRate) {
        this.openRatingDialog(rideId);
      } else {
        this.ratingRideId = null;
      }
    });
  }

  openNotifications(): void {
    if (!this.isLoggedIn) return;

  this.dialog.open(NotificationInbox, {
      autoFocus: false,
      panelClass: 'notifDialog',
      width: '1000px',
      maxWidth: '95vw',
      height: '720px',
      maxHeight: '90vh',
    });
  }


  private openRatingDialog(rideId: number): void {
    if (this.ratingDialogOpen) return;

    this.ratingRideId = rideId;
    this.ratingDialogOpen = true;

    this.ratingDialogRef = this.dialog.open(RatingPanel, {
      width: '520px',
      maxWidth: '95vw',
      disableClose: true,
      autoFocus: false,
    });

    const cmp = this.ratingDialogRef.componentInstance;

    const sub1 = cmp.submit.subscribe((data: any) => {
      this.submitRating(data);
    });

    const sub2 = cmp.close.subscribe(() => {
      this.closeRatingDialog();
    });

    this.ratingDialogRef.afterClosed().subscribe(() => {
      sub1.unsubscribe();
      sub2.unsubscribe();

      this.ratingDialogOpen = false;
      this.ratingDialogRef = undefined;
      this.ratingRideId = null;
    });
  }

  private closeRatingDialog(): void {
    this.ratingDialogRef?.close();
  }

  private closeAllRatingUi(): void {
    this.confirmRef?.close(false);
    this.confirmRef = undefined;
    this.confirmOpen = false;

    this.closeRatingDialog();
  }

  // ===== UI actions =====

  openSupport(): void {
    if (!this.isLoggedIn) return;

    if (this.role === 'ADMIN') {
      this.dialog.open(AdminChat, {
        autoFocus: false,
        panelClass: 'supportDialog',
        width: '900px',
        maxWidth: '95vw',
      });
    } else {
      this.dialog.open(ChatDialog, {
        autoFocus: false,
        panelClass: 'supportDialog',
      });
    }
  }

  private submitRating(data: any): void {
    if (this.ratingRideId == null) return;

    const payload: CreateRatingDto = {
      driverRating: data.driverRating,
      vehicleRating: data.vehicleRating,
      comment: data.comment,
    };

    this.ratingService.rateRide(this.ratingRideId, payload).subscribe({
      next: () => {
        this.closeRatingDialog();
      },
      error: (err) => console.error('Rating failed:', err),
    });
  }

  setRegistrationOpen(value: boolean): void {
    this.isRegistrationOpen = value;
  }

  setActive(value: boolean): void {
    this.isActive = value;
  }

  openLogin(): void {
    this.isLoginOpen = true;

    const el = document.getElementById('estimation');
    if (el) {
      el.style.filter = 'blur(6px)';
      el.style.pointerEvents = 'none';
      el.style.userSelect = 'none';
    }
  }

  closeLogin(): void {
    this.isLoginOpen = false;

    const el = document.getElementById('estimation');
    if (el) {
      el.style.filter = 'blur(0px)';
      el.style.pointerEvents = 'auto';
      el.style.userSelect = 'auto';
    }
  }

  onLoggedIn(): void {
    this.closeLogin();
    this.setActive(true);

    this.refreshAuthState();
    this.ensureWsConnectedIfLoggedIn();

    if (this.isLoggedIn && this.role === 'PASSENGER') {
      this.checkPendingLatestAndOpenFlow();
    } else {
      this.closeAllRatingUi();
    }
  }

  logout(): void {
    localStorage.removeItem('user');
    this.authService.setUser();
    this.supportChatService.disconnect();

    this.closeAllRatingUi();

    this.isLoggedIn = false;
    this.isRegistrationOpen = false;
    this.setActive(false);
    this.role = '';

    this.router.navigate(['/HomePage']);
    this.mapService.showInitialVehicles();
  }

  openRegistration(): void {
    this.isRegistrationOpen = true;
  }

  closeRegistration(): void {
    this.isRegistrationOpen = false;
  }
}
