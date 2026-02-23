import { Component, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { FavouriteRouteCard } from '../favourite-route-card/favourite-route-card';
import { Route } from '../model/ui/route';
import { GetRouteDTO } from '../model/ui/get-route-dto';
import { AuthService } from '../infrastructure/auth.service';
import { FavouriteRoutesService } from '../service/favourite-routes-service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-favourite-routes',
  imports: [FavouriteRouteCard],
  templateUrl: './favourite-routes.html',
  styleUrl: './favourite-routes.css',
})
export class FavouriteRoutes {
  @ViewChild('carousel', { static: true })
  carousel!: ElementRef<HTMLDivElement>;
  favouriteRoutes:GetRouteDTO[] = [];
  routes:Route[] = [];

  constructor(private authService:AuthService, private favRouteService:FavouriteRoutesService, private cdr:ChangeDetectorRef, private snackbar:MatSnackBar){}

  ngOnInit(){
    this.favRouteService.getFavouriteRoutes(this.authService.getId()).subscribe({
      next: (favRoutes) => {
        this.routes = favRoutes.map(route => this.mapGetRouteDTOToRoute(route));
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

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

  private mapGetRouteDTOToRoute(dto: GetRouteDTO): Route {
    const locations = dto.locations;

    return {
      id: dto.id,
      startingPoint: locations[0]?.address ?? '',
      stoppingPoints: locations.slice(1, locations.length - 1).map(l => l.address),
      destination: locations[locations.length - 1]?.address ?? '',
    };
  }

  onRemove(routeId:number){
    this.favRouteService.deleteFavouriteRoutes(this.authService.getId(), routeId).subscribe({
      next: () => {
        this.snackbar.open("Route successfully removed", "Close", {duration:4000});
        this.favRouteService.getFavouriteRoutes(this.authService.getId()).subscribe({
          next: (favRoutes) => {
            this.routes = favRoutes.map(route => this.mapGetRouteDTOToRoute(route));
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.log(err);
          }
        });
      },
      error: (err) => {
        console.log(err);
      }
    })
  }
}
