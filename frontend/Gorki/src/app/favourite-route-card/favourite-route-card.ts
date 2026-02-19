import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Route } from '../model/ui/route';
import { Router } from "@angular/router";

@Component({
  selector: 'app-favourite-route-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './favourite-route-card.html',
  styleUrl: './favourite-route-card.css',
})
export class FavouriteRouteCard {
  @Input() route!: Route;
  @Output() remove = new EventEmitter<number>();
  constructor(private router: Router) {}

  removeRoute(){
    this.remove.emit(this.route.id);
  }

  choose() {
    this.router.navigate(['/HomePage'], { state: { selectedRoute: this.route } });
  }
}
