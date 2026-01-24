import { Component, OnInit } from '@angular/core';
import { MapComponent } from "../map/map";
import { TrackRide } from '../rides/track-ride/track-ride';
import { MapService } from '../map/map-service';

@Component({
  selector: 'app-ride-in-progress',
  standalone: true,
  imports: [MapComponent, TrackRide],
  templateUrl: './ride-in-progress.html',
  styleUrls: ['./ride-in-progress.css'],
})
export class RideInProgress implements OnInit {

  constructor(private mapService: MapService) {}

  async ngOnInit() {
    const pickupAddress = 'Futoška 13a, Novi Sad';
    const dropoffAddress = 'Hadži Ruvimova 45, Novi Sad';

    const pickupCoords = await this.geocode(pickupAddress);
    const dropoffCoords = await this.geocode(dropoffAddress);

    this.mapService.showSnappedRoute(
    pickupCoords,
    dropoffCoords
  );
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
