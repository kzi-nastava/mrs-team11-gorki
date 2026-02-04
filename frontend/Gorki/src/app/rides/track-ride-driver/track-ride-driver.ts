import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MapService } from '../../map/map-service';
import { ChangeDetectorRef } from '@angular/core';
import { PanicService } from '../../service/panic-service';
import { StopRideService } from '../../service/stop-ride-service';

@Component({
  selector: 'app-track-ride-driver',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './track-ride-driver.html',
  styleUrl: './track-ride-driver.css',
})
export class TrackRideDriver {

  showReportModal = false;
  noteText = '';
  currentRideId: number = 2;

  @Input() pickupAddress = '';
  @Input() dropoffAddress = '';

  @Output() dropoffChanged = new EventEmitter<string>();

  constructor(
    private mapService: MapService, 
    private cdr: ChangeDetectorRef, 
    private panicService: PanicService, 
    private stopRideService: StopRideService) {}

  updateDropoff(address: string) {
    this.dropoffAddress = address;
  }

  async stopRide() {
    const coords = this.mapService.stopDriverAnimation();
    if (!coords) return;

    const address = await this.reverseGeocode(coords[0], coords[1]);

    this.dropoffChanged.emit(address);

    this.cdr.detectChanges(); 

    this.stopRideService.stopRide(this.currentRideId, {
      latitude: coords[0],
      longitude: coords[1],
      address
    }).subscribe({
      next: (res) => {
        alert(`Ride stopped.`);
      },
      error: (err) => alert(err?.error?.message ?? err?.error ?? 'Stop failed')
    });
  }

  async reverseGeocode(lat: number, lon: number): Promise<string> {
    const url =
      'https://nominatim.openstreetmap.org/reverse?' +
      new URLSearchParams({
        lat: lat.toString(),
        lon: lon.toString(),
        format: 'json',
        addressdetails: '1',
      });

    const res = await fetch(url);
    const data = await res.json();

    const a = data.address;

    const street =
      a.road ||
      a.pedestrian ||
      a.residential ||
      a.path ||
      '';

    const number =
      a.house_number || '';

    const city =
      a.city ||
      a.town ||
      a.village ||
      'Novi Sad';

    return number
      ? `${street} ${number}, ${city}`
      : `${street}, ${city}`;
  }

  panic() {
    this.panicService.triggerPanic(this.currentRideId).subscribe({
      next: () => {
        this.mapService.setDriverToPanic();
        alert('PANIC recorded!');
      },
      error: (err) => {
        alert(err?.error?.message ?? err?.error ?? 'PANIC failed');
      }
    });
  }
  
}
