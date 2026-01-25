import { Component,OnInit } from '@angular/core';
import { MapComponent } from "../../map/map";
import { MapService } from '../../map/map-service';
import { EndOfRidePanel } from "../end-of-ride-panel/end-of-ride-panel";
import { RideDetails } from "../ride-details/ride-details";

@Component({
  selector: 'app-end-of-ride',
  imports: [MapComponent, EndOfRidePanel],
  templateUrl: './end-of-ride.html',
  styleUrl: './end-of-ride.css',
})
export class EndOfRide implements OnInit {

  constructor(private mapService: MapService) {}

  async ngOnInit(){
    this.mapService.clearAll();
    this.mapService.clearRoute();
    const pickupAddress = 'Jerneja Kopitara 32, Novi Sad';
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

}
