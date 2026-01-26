import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Route } from '../model/ui/route';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-favourite-route-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './favourite-route-card.html',
  styleUrl: './favourite-route-card.css',
})
export class FavouriteRouteCard {
  @Input() route!: Route;
}
