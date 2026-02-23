import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserHistoryRide } from '../models/ride';
import { FavouriteRoutesService } from '../../service/favourite-routes-service';
import { AuthService } from '../../infrastructure/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-ride-card-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ride-card-map.html',
  styleUrl: './ride-card-map.css'
})
export class RideCardMap {
  @Input() ride!: UserHistoryRide;
  @Output() openRideDetails = new EventEmitter<UserHistoryRide>();

  constructor(private favouriteRouteService:FavouriteRoutesService, private authService:AuthService, private snackbar:MatSnackBar){}

  addRouteToFavourites(){
    this.favouriteRouteService.addFavouriteRoutes(this.authService.getId(), this.ride.id).subscribe({
      next: () => {
        this.snackbar.open("Route added to favourites", "Close", {duration:4000});
      },
      error: (err) => {
        if(err.status == 409){
          this.snackbar.open("Route already added to favourites", "Close", {duration:4000});
          console.log(err);
        }
        else{
          this.snackbar.open("Unexpected error occured", "Close", {duration:4000});
          console.log(err);
        }
      }
    });
  }

  showDetails() {
    this.openRideDetails.emit(this.ride);
  }
}