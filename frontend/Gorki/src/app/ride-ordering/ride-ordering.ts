import { Component } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { NgClass, NgIf } from "@angular/common";
import { ReactiveFormsModule, FormArray, FormControl, FormGroup } from '@angular/forms';
import { RouterLink } from "@angular/router";
import { OrderingConfirmation } from '../ordering-confirmation/ordering-confirmation';
import { OrderRideService } from '../service/order-ride-service';
import { Validators } from '@angular/forms';
import { CreateRouteDTO } from '../model/ui/create-route-dto';
import { CreateRideDTO } from '../model/ui/create-ride-dto';
import { AuthService } from '../infrastructure/auth.service';

@Component({
  selector: 'app-ride-ordering',
  imports: [MatRadioModule, MatFormFieldModule, MatSelectModule, NgClass, ReactiveFormsModule, RouterLink, OrderingConfirmation],
  templateUrl: './ride-ordering.html',
  styleUrl: './ride-ordering.css',
})
export class RideOrdering {
  activeForm: 'main' | 'stopping' | 'passengers' = 'main';
  createdPrice!: number;
  mainForm = new FormGroup({
    startAddress: new FormControl<string>('', { nonNullable: true, validators: [Validators.required] }),
    endAddress: new FormControl<string>('', { nonNullable: true, validators: [Validators.required] }),
    scheduledTime: new FormControl<string>('', { nonNullable: false }),
    vehicleType: new FormControl<'STANDARD'|'LUXURY'|'VAN'>('STANDARD', { nonNullable: true }),
    babyTransport: new FormControl<boolean>(false, { nonNullable: true }),
    petTransport: new FormControl<boolean>(false, { nonNullable: true }),
  });
  stoppingPointsForm = new FormGroup({
    points: new FormArray([ new FormControl('') ])
  });
  otherPassengersForm = new FormGroup({
    passengers: new FormArray([new FormControl('')])
  });
  isModalOpen: boolean = false;

  constructor(private orderingService:OrderRideService, private authService: AuthService){}

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

  async order() {
    if (this.mainForm.invalid) {
      this.mainForm.markAllAsTouched();
      alert('Start and end address are required.');
      return;
    }

    const points = this.points
      .map(c => (c.value ?? '').trim())
      .filter(v => v.length > 0);

    const passengers = this.passengers
      .map(c => (c.value ?? '').trim())
      .filter(v => v.length > 0);

    const v = this.mainForm.getRawValue();

    try {
      const [startLat, startLon] = await this.geocode(v.startAddress);
      const [endLat, endLon] = await this.geocode(v.endAddress);
      const stoppingCoords = await Promise.all(
        points.map(p => this.geocode(p))
      );
      const route: CreateRouteDTO = {
        locations: [
          {
            address: v.startAddress,
            latitude: startLat,
            longitude: startLon,
          },
          ...points.map((addr, i) => ({
            address: addr,
            latitude: stoppingCoords[i][0],
            longitude: stoppingCoords[i][1],
          })),
          {
            address: v.endAddress,
            latitude: endLat,
            longitude: endLon,
          },
        ],
      };

      // Ako tvoj CreateRideDTO očekuje route:
      const dto: CreateRideDTO = {
        route: route,
        linkedPassengersEmails: passengers,
        creatorId: this.authService.getId(),
        babyTransport: v.babyTransport,
        petFriendly: v.petTransport,
        vehicleType: v.vehicleType,
        scheduledTime: v.scheduledTime?.trim() ? v.scheduledTime.trim() : null,
      } as any;
      

      this.orderingService.orderRide(dto).subscribe({
        next: (created) => {
          alert("Ride ordered successfully!");
          this.isModalOpen = true;
          this.createdPrice = created.price;
        },
        error: (err) => {
          console.error(err);
          alert('Ride ordering failed.');
        },
      });
    } catch (e) {
      console.error(e);
      alert('One of the addresses could not be geocoded.');
    }
  }
  get points(): FormControl[] {
    return (this.stoppingPointsForm.get('points') as FormArray).controls as FormControl[];
  }

  get pointss(): FormArray {
    return this.stoppingPointsForm.get('points') as FormArray;
  }

  get passengers(): FormControl[] {
    return (this.otherPassengersForm.get('passengers') as FormArray).controls as FormControl[];
  }

  get passengerss(): FormArray{
    return this.otherPassengersForm.get('passengers') as FormArray;
  }

  addStoppingPoint() {
    this.pointss.push(new FormControl(''));
  }

  removeStoppingPoint(index: number) {
    this.pointss.removeAt(index);
  }

  addPassenger() {
    this.passengerss.push(new FormControl(''));
  }

  removePassenger(index: number) {
    this.passengerss.removeAt(index);
  }

  openStoppingForm() {
    this.activeForm = 'stopping';
  }

  openPassengersForm() {
    this.activeForm = 'passengers';
  }

  backToMain() {
    this.activeForm = 'main';
  }
  onConfirm() {
    this.isModalOpen = false;
    this.mainForm.reset({ vehicleType: 'STANDARD', babyTransport: false, petTransport: false, scheduledTime: '', startAddress: '', endAddress: ''});
    this.pointss.clear();
    this.pointss.push(new FormControl(''));
    this.passengerss.clear();
    this.passengerss.push(new FormControl(''));
    this.activeForm = 'main';
  }
}
