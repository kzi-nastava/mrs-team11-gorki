import { Component, OnInit } from '@angular/core';
import { MapComponent } from "../map/map";
import { TrackRide } from '../rides/track-ride/track-ride';
import { MapService } from '../map/map-service';
import { RideInProgressService } from '../service/passenger-ride-in-progress';
import { AuthService } from '../infrastructure/auth.service';

@Component({
  selector: 'app-ride-in-progress',
  standalone: true,
  imports: [MapComponent, TrackRide],
  templateUrl: './ride-in-progress.html',
  styleUrls: ['./ride-in-progress.css'],
})
export class RideInProgress implements OnInit {

  pickupAddress: string = '';
  dropoffAddress: string = '';

  constructor(
    private mapService: MapService,
    private rideService: RideInProgressService,
    private authService:AuthService
  ) {}

  async ngOnInit() {
    this.mapService.clearAll();

    // Dohvati voznju u toku za putnika id->localstorage
    this.rideService.getActiveRideAddresses(this.authService.getId()).subscribe({
      next: async ({ pickup, dropoff }) => {
        this.pickupAddress = pickup;
        this.dropoffAddress = dropoff;

        const pickupTextEl = document.getElementById('PickupText');
        const dropoffTextEl = document.getElementById('DropoffText');

        if (pickupTextEl) pickupTextEl.textContent = pickup;
        if (dropoffTextEl) dropoffTextEl.textContent = dropoff;
        

        const pickupCoords = await this.geocode(pickup);
        const dropoffCoords = await this.geocode(dropoff);

        await this.mapService.showSnappedRoute(pickupCoords, dropoffCoords);

        this.mapService.showCarAnimation();
      },
      error: err => {
        console.error('Ne mogu da dohvatim aktivnu voznju', err);
      }
    });
  }

  async geocode(address: string): Promise<[number, number]> {

    const res = await fetch(
      `http://localhost:8080/api/geocode?q=${encodeURIComponent(address)}`
    );
    const data = await res.json();
    return [data.lat, data.lon];
  }

  handleShowOnMap(event: {
    pickupCoords: [number, number];
    dropoffCoords: [number, number];
  }) {
    this.mapService.showSnappedRoute(
      event.pickupCoords,
      event.dropoffCoords
    );
  }
}
