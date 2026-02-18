import { Component, ViewChild, ElementRef } from '@angular/core';
import { FavouriteRouteCard } from '../favourite-route-card/favourite-route-card';
import { Route } from '../model/ui/route';

@Component({
  selector: 'app-favourite-routes',
  imports: [FavouriteRouteCard],
  templateUrl: './favourite-routes.html',
  styleUrl: './favourite-routes.css',
})
export class FavouriteRoutes {
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

  routes:Route[] = [
    {
      id: 1,
      startingPoint: 'Miše Dimitrijevića 5, Grbavica',
      stoppingPoints: [
        'Ive Andrica 15, Liman 4',
      ],
      destination: 'Jerneja Kopitara 32, Telep'
    },
    {
      id: 2,
      startingPoint: 'Miše Dimitrijevića 5, Grbavica',
      stoppingPoints: [
        'Ive Andrica 15, Liman 4',
      ],
      destination: 'Jerneja Kopitara 32, Telep'
    },
    {
      id: 3,
      startingPoint: 'Miše Dimitrijevića 5, Grbavica',
      stoppingPoints: [
        'Ive Andrica 15, Liman 4',
      ],
      destination: 'Jerneja Kopitara 32, Telep'
    },
  ];
}
