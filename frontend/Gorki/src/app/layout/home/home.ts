import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MapComponent } from '../../map/map';
import { AuthService } from '../../infrastructure/auth.service';
import { RideEstimateCardComponent } from '../../ride-estimate-unuser/ride-estimate-unuser';
import { MapService } from '../../map/map-service';
import { RideOrdering } from '../../ride-ordering/ride-ordering';
import { RideStart } from '../../ride-start/ride-start';
import { Subscription } from 'rxjs';
import { GetUserDTO } from '../../model/ui/get-user-dto';
import { UserProfileService } from '../../service/user-profile-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    MapComponent,
    RideEstimateCardComponent,
    RideOrdering,
    RideStart,
    FormsModule
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home implements OnInit {
  role: string = "";
  user!: GetUserDTO;
  isBlocked: boolean = false;
  private sub?: Subscription;

  constructor(public auth: AuthService,private mapService: MapService, private userService:UserProfileService, private cdr:ChangeDetectorRef) {}

  ngOnInit(){
    this.mapService.showInitialVehicles();
    this.role = this.auth.getRole();
    this.sub = this.auth.userState.subscribe(() => {
      this.role = this.auth.getRole();
      if(this.role == 'PASSENGER'){
        const id = this.auth.getId();
        this.userService.getUserInfo(id).subscribe({
          next: (user) => {
            this.user = user;
            this.isBlocked = !!user.blocked;
            this.cdr.detectChanges();
          }
        });
      }
    });
  }

  ngOnDestroy(){
    this.sub?.unsubscribe();
  }

  onEstimateRide() {
    console.log('Estimate ride clicked');
  }

  handleShowOnMap(event: {
      pickupCoords: [number, number];
      dropoffCoords: [number, number];
    }) {
      this.mapService.showSnappedRoute(event.pickupCoords, event.dropoffCoords);
      
  }
}
