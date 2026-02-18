import { ChangeDetectorRef, Component } from '@angular/core';
import { StartRideService } from '../service/start-ride-service';
import { AuthService } from '../infrastructure/auth.service';
import { GetRideDTO } from '../model/ui/get-ride-dto';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-ride-start',
  imports: [],
  templateUrl: './ride-start.html',
  styleUrl: './ride-start.css',
})
export class RideStart {
  isModalOpen: boolean = false;

  constructor(private startRideService: StartRideService, private authService: AuthService, private cdr:ChangeDetectorRef, private snackbar:MatSnackBar){}

  ride?: GetRideDTO | null;

  ngOnInit(){
    this.startRideService.getNextScheduledRide(this.authService.getId()).subscribe(ride => {
      this.ride = ride;
      this.cdr.detectChanges();
    });
  }

  start(){
    const id = this.ride?.id;
    if(id == null){
      return
    }
    this.startRideService.startRide(id).subscribe({
      next: () => {
        this.snackbar.open('Ride started successfully!', 'Close', {duration:4000});
      },
      error: (err) => console.log(err)
    });
  }

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

}
