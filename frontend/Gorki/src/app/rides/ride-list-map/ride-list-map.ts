import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MapComponent } from '../../map/map'; 
import { MapService } from '../../map/map-service';
import { UserHistoryRide } from '../models/ride';
import { ActivatedRoute } from '@angular/router';
import { RideCardMap } from '../ride-card-map/ride-card-map';
import { RideDetailsAdmin } from '../ride-details-admin/ride-details-admin';

@Component({
  selector: 'app-ride-list-map',
  standalone: true,
  imports: [FormsModule, CommonModule, MapComponent, RideCardMap, RideDetailsAdmin],
  templateUrl: './ride-list-map.html',
  styleUrl: './ride-list-map.css',
})
export class RideListMap {

  showReportModal = false;
  noteText = '';
  ride: UserHistoryRide | null = null;
  rideId!: number;
  rideToShow!: UserHistoryRide;
  showRideDetails = false;

  constructor( private route: ActivatedRoute, private mapService: MapService) {}

  async ngOnInit() {
    const navigation = history.state.ride as UserHistoryRide;
    if (!navigation) {
      console.error('Ride not passed!');
      return;
    }
    this.rideToShow = navigation;

    // prikaži rutu automatski
    const pickupCoords = await this.geocode(this.rideToShow.startLocation);
    const dropoffCoords = await this.geocode(this.rideToShow.destination);
    await this.mapService.showSnappedRoute(pickupCoords, dropoffCoords);
  }

  // Geocode metoda ista kao u RideInProgress
  async geocode(address: string): Promise<[number, number]> {

    const res = await fetch(
      `http://localhost:8080/api/geocode?q=${encodeURIComponent(address)}`
    );
    const data = await res.json();
    return [data.lat, data.lon];
  }

  goBack() {
    window.history.back();
  }

  openRideDetails(ride: UserHistoryRide) {
    this.showRideDetails = true;
  }
  
  closeRideDetails() {
    this.showRideDetails = false;
  }

}