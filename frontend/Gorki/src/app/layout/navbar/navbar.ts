import { Component } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { RouterLink } from '@angular/router';
//import { Login } from '../login/login';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatMenuModule, RouterLink, /*Login*/],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css'], 
})

export class Navbar {
  
  isLoggedIn: boolean = false;
  isRegistrationOpen: boolean = false;
  isLoginOpen: boolean = false;

  role: string = "unuser";
  
  openLogin() { 
    this.isLoginOpen = true; 
  }

  closeLogin() { 
    this.isLoginOpen = false;
  }

  onLoggedIn() {
    this.isLoggedIn = true;
    this.role = "user";
  }

}