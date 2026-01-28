import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MapComponent } from '../../map/map';
import { AuthService } from '../../auth/auth';
import { RideEstimateCardComponent } from '../../ride-estimate-unuser/ride-estimate-unuser';
import { MapService } from '../../map/map-service';
import { RideOrdering } from '../../ride-ordering/ride-ordering';
import { RideStart } from '../../ride-start/ride-start';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    MapComponent,
    RideEstimateCardComponent,
    RideOrdering,
    RideStart
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home implements OnInit {
  role: string = "unuser";
  constructor(public auth: AuthService,private mapService: MapService) {}

  ngOnInit(){
    this.mapService.showInitialVehicles();
  }
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
