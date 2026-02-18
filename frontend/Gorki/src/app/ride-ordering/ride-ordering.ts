import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { CommonModule, NgClass, NgIf } from "@angular/common";
import { ReactiveFormsModule, FormArray, FormControl, FormGroup } from '@angular/forms';
import { Router, RouterLink } from "@angular/router";
import { OrderingConfirmation } from '../ordering-confirmation/ordering-confirmation';
import { OrderRideService } from '../service/order-ride-service';
import { Validators } from '@angular/forms';
import { CreateRouteDTO } from '../model/ui/create-route-dto';
import { CreateRideDTO } from '../model/ui/create-ride-dto';
import { AuthService } from '../infrastructure/auth.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, startWith, switchMap, catchError } from 'rxjs/operators';
import { NominatimService } from '../service/nominatim-service';

@Component({
  selector: 'app-ride-ordering',
  imports: [CommonModule, MatRadioModule, MatFormFieldModule, MatSelectModule, MatInputModule, MatAutocompleteModule, NgClass, ReactiveFormsModule, RouterLink, OrderingConfirmation, MatSnackBarModule],
  templateUrl: './ride-ordering.html',
  styleUrl: './ride-ordering.css',
})
export class RideOrdering {
  @Input() isBlocked!: boolean;
  activeForm: 'main' | 'stopping' | 'passengers' = 'main';
  createdPrice!: number;
  startOptions$!: Observable<string[]>;
  endOptions$!: Observable<string[]>;
  stopOptions$: Observable<string[]>[] = [];
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

  constructor(private orderingService:OrderRideService, private authService: AuthService, private snackBar:MatSnackBar, private cdr:ChangeDetectorRef, private nominatim:NominatimService){}

  private addressOptionsFor(control: FormControl<string | null>): Observable<string[]> {
    return control.valueChanges.pipe(
      startWith(control.value ?? ''),
      map(v => (v ?? '').trim()),
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => {
        if (term.length < 3) return of([]);
        return this.nominatim.search(term, 6).pipe(
          map(items => items.map(i => i.display_name)),
          catchError(() => of([]))
        );
      })
    );
  }

  private applyFavouriteRoute(route: { startingPoint: string; destination: string; stoppingPoints?: string[] }) {
    this.mainForm.patchValue({
      startAddress: route.startingPoint ?? '',
      endAddress: route.destination ?? '',
    });

    const stops = route.stoppingPoints ?? [];

    this.pointss.clear();
    this.stopOptions$ = [];

    if (stops.length === 0) {
      const ctrl = new FormControl<string>('', { nonNullable: true });
      this.pointss.push(ctrl);
      this.stopOptions$.push(this.addressOptionsFor(ctrl));
    } else {
      for (const s of stops) {
        const ctrl = new FormControl<string>(s, { nonNullable: true });
        this.pointss.push(ctrl);
        this.stopOptions$.push(this.addressOptionsFor(ctrl));
      }
    }

    this.activeForm = 'main';
  }

  ngOnInit(): void {
    this.startOptions$ = this.addressOptionsFor(this.mainForm.controls.startAddress);
    this.endOptions$ = this.addressOptionsFor(this.mainForm.controls.endAddress);
    this.stopOptions$ = this.points.map(ctrl => this.addressOptionsFor(ctrl as any));
    const selected = history?.state.selectedRoute as any;
    if(selected){
      this.applyFavouriteRoute(selected);
      window.history.replaceState({}, '', window.location.pathname)
    }
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

  async order() {
    if (this.mainForm.invalid) {
      this.mainForm.markAllAsTouched();
      this.snackBar.open('Start and end address are required.', 'Close', {duration:4000});
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
          this.snackBar.open("Ride ordered successfully!", 'Close', {duration:4000})
          this.createdPrice = created.price;
          this.isModalOpen = true;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open("Ride ordering failed.", 'Close', {duration:4000})
        },
      });
    } catch (e) {
      console.error(e);
      this.snackBar.open('One of the addresses could not be geocoded.', 'Close', {duration:4000});
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
    const ctrl = new FormControl<string>('', { nonNullable: true });
    this.pointss.push(ctrl);
    this.stopOptions$.push(this.addressOptionsFor(ctrl));
  }

  removeStoppingPoint(index: number) {
    this.pointss.removeAt(index);
    this.stopOptions$.splice(index, 1);
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
