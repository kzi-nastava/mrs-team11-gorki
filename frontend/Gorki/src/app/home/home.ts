import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MapComponent } from '../map/map';
import { AuthService } from '../auth/auth';
import { RideEstimateCardComponent } from '../ride-estimate-unuser/ride-estimate-unuser';
import { MapService } from '../map/map-service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    MapComponent,
    RideEstimateCardComponent,
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home {
  constructor(public auth: AuthService,private mapService: MapService) {}

  onEstimateRide() {
    console.log('Estimate ride clicked');
  }

  handleShowOnMap(event: {
      pickupCoords: [number, number];
      dropoffCoords: [number, number];
    }) {
      this.mapService.showSnappedRoute(event.pickupCoords, event.dropoffCoords);
      
  }
  
}