import { Component } from '@angular/core';
import { OrderedRide } from '../model/ui/orderedRide';

@Component({
  selector: 'app-ride-start',
  imports: [],
  templateUrl: './ride-start.html',
  styleUrl: './ride-start.css',
})
export class RideStart {
  isModalOpen: boolean = false;
  ride: OrderedRide = {
    route: {
      id: 1,
      startingPoint: 'Miše Dimitrijevića 5, Grbavica',
      stoppingPoints: [
        'Ive Andrica 15, Liman 4',
      ],
      destination: 'Jerneja Kopitara 32, Telep'
    },
    creator: {
      email:'marko.pavlovic2404004@gmail.com',
      firstName:'Marko',
      lastName:'Pavlovic',
      phoneNumber:381648816145,
      address:'Mornarska 51, Novi Sad',
      profileImage:'/user-pic.png',
    },
    linkedPassengers: [
    {
      email:'ognjen.miletic@gmail.com',
      firstName:'Ognjen',
      lastName:'Miletic',
      phoneNumber:381647612354,
      address:'Mornarska 51, Novi Sad',
      profileImage:'/user-pic.png',
    },
    {
      email:'luka.beric@gmail.com',
      firstName:'Luka',
      lastName:'Beric',
      phoneNumber:381649356741,
      address:'Mornarska 51, Novi Sad',
      profileImage:'/user-pic.png',
    }],
    scheduledTime: '14.05',
    price: 10.00
  };

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

}
