import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DriverScheduledRide, Passenger } from '../models/ride';
import { DriverScheduledRideCard } from '../driver-scheduled-ride-card/driver-scheduled-ride-card';
import { DriverScheduledRideDetails } from '../driver-scheduled-ride-details/driver-scheduled-ride-details';

@Component({
  selector: 'app-driver-scheduled-rides-list',
  imports: [CommonModule,DriverScheduledRideCard,DriverScheduledRideDetails ],
  templateUrl: './driver-scheduled-rides-list.html',
  styleUrl: './driver-scheduled-rides-list.css',
})
export class DriverScheduledRidesList {
    selectedRide: DriverScheduledRide | null = null;
    @ViewChild('carousel', { static: true })
    carousel!: ElementRef<HTMLDivElement>;

  scrollLeft() {
    this.carousel.nativeElement.scrollBy({
      left: -300,
      behavior: 'smooth',
    });
  }

  scrollRight() {
    this.carousel.nativeElement.scrollBy({
      left: 300,
      behavior: 'smooth',
    });
  }

  scheduledRides: DriverScheduledRide[] = [
  {
    id: 1,
    rating: 0,
    startTime: '17:05',
    startLocation: 'Miše Dimitrijevića 5, Grbavica',
    destination: 'Jerneja Kopitara 32, Telep',
    price: 15,
    date: new Date(2026, 0, 26),
    canceled: false,
    cancelationReason: 'None',
    panic: false,
    passengers: [
      { email: 'ivan@example.com', firstName: 'Ivan', lastName: 'Ivić', phoneNumber: '0601234567' },
      { email: 'ana@example.com', firstName: 'Ana', lastName: 'Anić', phoneNumber: '0612345678' }
    ]
  },
  {
    id: 2,
    rating: 0,
    startTime: '09:30',
    startLocation: 'Bulevar Oslobođenja 88',
    destination: 'Trg Slobode',
    price: 8,
    date: new Date(2026, 0, 27),
    canceled: false,
    cancelationReason: 'None',
    panic: false,
    passengers: [
      { email: 'marko@example.com', firstName: 'Marko', lastName: 'Marković', phoneNumber: '062111222' }
    ]
  },
  {
    id: 3,
    rating: 0,
    startTime: '14:10',
    startLocation: 'Cara Dušana 12',
    destination: 'Futoški put 93',
    price: 12,
    date: new Date(2026, 0, 17),
    canceled: true,
    cancelationReason: 'Passenger no-show',
    panic: false,
    passengers: [
      { email: 'jelena@example.com', firstName: 'Jelena', lastName: 'Jovanović', phoneNumber: '063333444' }
    ]
  },
  {
    id: 4,
    rating: 0,
    startTime: '20:45',
    startLocation: 'Detelinara, Rumenačka 45',
    destination: 'Liman IV',
    price: 10,
    date: new Date(2026, 0, 25),
    canceled: false,
    cancelationReason: 'None',
    panic: true,
    passengers: [
      { email: 'nikola@example.com', firstName: 'Nikola', lastName: 'Nikolić', phoneNumber: '064555666' },
      { email: 'sara@example.com', firstName: 'Sara', lastName: 'Simić', phoneNumber: '065777888' }
    ]
  },
  {
    id: 5,
    rating: 0,
    startTime: '07:15',
    startLocation: 'Avijatičarsko naselje',
    destination: 'Železnička stanica',
    price: 6,
    date: new Date(2026, 0, 24),
    canceled: false,
    cancelationReason: 'None',
    panic: false,
    passengers: [
      { email: 'petar@example.com', firstName: 'Petar', lastName: 'Petrović', phoneNumber: '066999000' }
    ]
  },
  {
    id: 6,
    rating: 0,
    startTime: '18:00',
    startLocation: 'Podbara, Beogradska 3',
    destination: 'Klisa',
    price: 9,
    date: new Date(2026, 0, 25),
    canceled: true,
    cancelationReason: 'Driver emergency',
    panic: true,
    passengers: [
      { email: 'milica@example.com', firstName: 'Milica', lastName: 'Milić', phoneNumber: '061222333' }
    ]
  }
];

  openDetails(ride:DriverScheduledRide){
    this.selectedRide=ride;
  }

  closeDetails() {
    this.selectedRide = null;
  }

  cancelSelectedRide(ride:DriverScheduledRide){
    if(!ride.canceled && ride.date.getTime() > Date.now()){
      ride.canceled = true;
      ride.cancelationReason = "Some";
    }
    /*DODATI JOS NEKU LOGIKU*/
  }
}
