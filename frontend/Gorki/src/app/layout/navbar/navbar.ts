import { Component } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Login } from '../../login/login';
import { MatButtonToggleModule } from '@angular/material/button-toggle';

type Role = 'admin' | 'driver' | 'user' | 'unuser';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatMenuModule, RouterOutlet, RouterLink, Login, MatButtonToggleModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css'], 
})



export class Navbar { 
  isLoggedIn: boolean = false;
  isRegistrationOpen: boolean = false;
  isLoginOpen: boolean = false;
  isActive: boolean = true;
  role: Role = "unuser";

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

  onLoggedIn(role: Role) {
    this.isLoggedIn = true;
    this.role = role;
    this.setActive(true);
  }

  logout(){
    this.isLoggedIn = false;
    this.isRegistrationOpen = false;
    this.setActive(false);
    this.role = "unuser";
  }

  openRegistration(){
    this.isRegistrationOpen = true;
  }

  closeRegistration(){
    this.isRegistrationOpen = false;
  }
}