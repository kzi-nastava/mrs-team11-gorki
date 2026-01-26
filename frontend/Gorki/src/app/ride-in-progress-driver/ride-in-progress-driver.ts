import { Component, OnInit } from '@angular/core';
import { MapComponent } from "../map/map";
import { MapService } from '../map/map-service';
import { TrackRideDriver } from '../rides/track-ride-driver/track-ride-driver';

@Component({
  selector: 'app-ride-in-progress-driver',
  standalone: true,
  imports: [MapComponent, TrackRideDriver],
  templateUrl: './ride-in-progress-driver.html',
  styleUrls: ['./ride-in-progress-driver.css'],
})
export class RideInProgressDriver implements OnInit {

  constructor(private mapService: MapService) {}

  async ngOnInit() {
    this.mapService.clearAll();
    //this.mapService.clearVehicles();

    const pickupAddress = 'Futoška 13a, Novi Sad';
    const dropoffAddress = 'Hadži Ruvimova 45, Novi Sad';

    const pickupCoords = await this.geocode(pickupAddress);
    const dropoffCoords = await this.geocode(dropoffAddress);

    await this.mapService.showSnappedRoute(pickupCoords, dropoffCoords);

    // 🔥 NEMA ARGUMENATA
    this.mapService.showCarAnimation()
  }

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
