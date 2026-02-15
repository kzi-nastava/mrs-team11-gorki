import { Component } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { Login } from '../../login/login';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { Subscription } from 'rxjs';
import { AuthService } from '../../infrastructure/auth.service';
import { MapService } from '../../map/map-service';
import { MatDialog } from '@angular/material/dialog';
import { ChatDialog } from '../../live-chat/users/chat-dialog/chat-dialog';
import { AdminChat } from '../../live-chat/admin/admin-chat/admin-chat';

type Role = 'admin' | 'driver' | 'user' | 'unuser';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatMenuModule,MatIconModule, RouterOutlet, RouterLink, Login, MatButtonToggleModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css'], 
})
export class Navbar { 
  isRegistrationOpen: boolean = false;
  isLoginOpen: boolean = false;
  isActive: boolean = true;

  isLoggedIn: boolean = false;
  role: string = "";
  
  private sub?: Subscription;

  constructor(private authService:AuthService, private router: Router,private mapService:MapService,private dialog: MatDialog ){}

  openSupport() {

    if (this.role === 'ADMIN') {

      this.dialog.open(AdminChat, {
        autoFocus: false,
        panelClass: 'supportDialog',
        width: '900px',
        maxWidth: '95vw'
      });

    } else {

      this.dialog.open(ChatDialog, {
        autoFocus: false,
        panelClass: 'supportDialog',
      });

    }
  }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.role = this.authService.getRole();

    this.sub = this.authService.userState.subscribe(() => {
      this.isLoggedIn = this.authService.isLoggedIn();
      this.role = this.authService.getRole();
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  setRegistrationOpen(value: boolean){
    this.isRegistrationOpen = value;
  }
  
  setActive(value: boolean) {
    this.isActive = value;
  }

  openLogin() { 
    this.isLoginOpen = true; 
    const el = document.getElementById('estimation');

    if (el) {
      el.style.filter = 'blur(6px)';
      el.style.pointerEvents = 'none';
      el.style.userSelect = 'none';
    }
  }

  closeLogin() { 
    this.isLoginOpen = false;
    const el = document.getElementById('estimation');
    if (el) {
      el.style.filter = 'blur(0px)';
      el.style.pointerEvents = 'auto';
      el.style.userSelect = 'auto';
    }
  }

  onLoggedIn() {
    this.closeLogin()
    this.setActive(true);
  }

  logout(){

    localStorage.removeItem('user');
    this.authService.setUser();
    this.isLoggedIn = false;
    this.isRegistrationOpen = false;
    this.setActive(false);
    this.role = "";
    this.router.navigate(["/HomePage"]);
    this.mapService.showInitialVehicles();
  }

  openRegistration(){
    this.isRegistrationOpen = true;
  }

  closeRegistration(){
    this.isRegistrationOpen = false;
  }
}