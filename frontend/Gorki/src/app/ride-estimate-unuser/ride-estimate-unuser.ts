import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-ride-estimate-unuser',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './ride-estimate-unuser.html',
  styleUrls: ['./ride-estimate-unuser.css']
})
export class RideEstimateCardComponent {

  @Output()
  estimateRide = new EventEmitter<{
    pickup: string;
    dropoff: string;
  }>();

  pickup = '';
  dropoff = '';

  haversineDistance(
    lat1: number,
    lon1: number,
    lat2: number,
    lon2: number): number {
    const R = 6371000; 
    const toRad = (deg: number): number => deg * Math.PI / 180;

    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);

    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(toRad(lat1)) *
        Math.cos(toRad(lat2)) *
        Math.sin(dLon / 2) *
        Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return (R * c)/1000; 
  }

  showEstimatePanel = false;
  estimatedTime: string | null = null;
  distance: number | null = null;

  @Output()
  showOnMap = new EventEmitter<{
    pickupCoords: [number, number];
    dropoffCoords: [number, number];
  }>();
  async geocode(address: string): Promise<[number, number]> {
    const url =
      'https://nominatim.openstreetmap.org/search?' +
      new URLSearchParams({
        q: address,
        format: 'json',
        limit: '1',
      });

    const res = await fetch(url, {
      headers: {
        'Accept': 'application/json',
      }
    });

    const data = await res.json();

    if (!data.length) {
      throw new Error('Address not found');
    }

    return [parseFloat(data[0].lat), parseFloat(data[0].lon)];
  }

  async onEstimateClick() {
    if (!this.pickup || !this.dropoff) {
      this.estimatedTime = "Invalid input, try again";
      this.showEstimatePanel = true;
      return;
    }


    this.showEstimatePanel = true;
    this.estimateRide.emit({
      pickup: this.pickup,
      dropoff: this.dropoff
    });

    try {
      const pickupCoords = await this.geocode(this.pickup);
      const dropoffCoords = await this.geocode(this.dropoff);
      this.distance=this.haversineDistance(pickupCoords[0],pickupCoords[1],dropoffCoords[0],dropoffCoords[1]);
      let speedKmH = 30;           // grad
      if (this.distance > 8) speedKmH = 40;   // prigrad 
      if (this.distance > 20) speedKmH = 60;   // otvoren put

      // + circuity faktor
      const factor = this.distance < 2 ? 1.25 : this.distance < 10 ? 1.35 : 1.45;

      const minutes = (this.distance * factor / speedKmH) * 60;
      this.estimatedTime =  Math.round(minutes) + ' min';;
      console.log('d:',this.distance);
      console.log('et:',this.estimatedTime); 
      this.showOnMap.emit({
        pickupCoords,
        dropoffCoords
      });
    } catch {
      console.error('Could not geocode one of the addresses');
    }
}
}