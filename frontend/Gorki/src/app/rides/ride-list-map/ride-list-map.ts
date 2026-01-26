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

  async showRouteForRide(ride: UserHistoryRide) {
    const pickupCoords = await this.geocode(ride.startLocation);
    const dropoffCoords = await this.geocode(ride.destination);

    this.mapService.clearRoute(); // briše prethodne rute
    await this.mapService.showSnappedRoute(pickupCoords, dropoffCoords);
  }

  // Geocode metoda ista kao u RideInProgress
  async geocode(address: string): Promise<[number, number]> {
    const url =
      'https://nominatim.openstreetmap.org/search?' +
      new URLSearchParams({
        q: address,
        format: 'json',
        limit: '1',
      });

    const res = await fetch(url);
    const data = await res.json();

    if (!data.length) {
      throw new Error('Address not found');
    }

    return [parseFloat(data[0].lat), parseFloat(data[0].lon)];
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