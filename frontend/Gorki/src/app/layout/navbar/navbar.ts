import { Component } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { RouterOutlet, RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [MatMenuModule, RouterOutlet, RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  isLoggedIn: boolean = true;
  isRegistrationOpen: boolean = false;
  role: string = "driver";
}
