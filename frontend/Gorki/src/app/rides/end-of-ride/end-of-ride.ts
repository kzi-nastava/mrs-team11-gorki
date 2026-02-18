import { Component,OnInit } from '@angular/core';
import { MapComponent } from "../../map/map";
import { MapService } from '../../map/map-service';
import { RideDetails } from "../ride-details/ride-details";
import { AuthService } from '../../infrastructure/auth.service';
import { DriverRideInProgress } from '../../service/driver-ride-in-progress';
import { EndOfRidePanel } from '../end-of-ride-panel/end-of-ride-panel';

@Component({
  selector: 'app-end-of-ride',
  imports: [MapComponent,EndOfRidePanel],
  templateUrl: './end-of-ride.html',
  styleUrl: './end-of-ride.css',
})
export class EndOfRide implements OnInit {

    constructor(
    private mapService: MapService,
    private driverService: DriverRideInProgress,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.driverService
      .getActiveRide(this.authService.getId())
      .subscribe(ride => {

        const pickupCoords: [number, number] = [
          ride.route.locations[0].latitude,
          ride.route.locations[0].longitude,
        ];

        const dropoffCoords: [number, number] = [
          ride.route.locations[1].latitude,
          ride.route.locations[1].longitude,
        ];

        this.mapService.showSnappedRoute(pickupCoords, dropoffCoords);
        this.mapService.showDriverEndPosition(dropoffCoords);
      });
  }
  

}
