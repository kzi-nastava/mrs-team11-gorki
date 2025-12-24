import { Component } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { RouterLink } from '@angular/router';
import { Login } from '../login/login';
import { MatButtonToggleModule } from '@angular/material/button-toggle';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatMenuModule, RouterLink, Login, MatButtonToggleModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css'], 
})

export class Navbar {
  
  isLoggedIn: boolean = false;
  isRegistrationOpen: boolean = false;
  isLoginOpen: boolean = false;
  isActive: boolean = true;

  role: string = "unuser";
  
  setActive(value: boolean) {
    this.isActive = value;
  }

  openLogin() { 
    this.isLoginOpen = true; 
  }

  closeLogin() { 
    this.isLoginOpen = false;
  }

  onLoggedIn() {
    this.isLoggedIn = true;
    this.role = "driver";
    this.setActive(true);
  }

  logout(){
    this.isLoggedIn = false;
    this.setActive(false);
    this.role = "unuser";
  }

}