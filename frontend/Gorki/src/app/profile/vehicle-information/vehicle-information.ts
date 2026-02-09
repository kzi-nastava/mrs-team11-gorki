import { Component } from '@angular/core';
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';
import { Vehicle } from '../../model/ui/vehicle';
import { FormsModule } from '@angular/forms';
import { GetVehicleDTO } from '../../model/ui/get-vehicle-dto';
import { VehicleInfoService } from '../../service/vehicle-info-service';

@Component({
  selector: 'app-vehicle-information',
  imports: [MatRadioModule, MatFormFieldModule, MatSelectModule, FormsModule],
  templateUrl: './vehicle-information.html',
  styleUrl: './vehicle-information.css',
})
export class VehicleInformation {
  vehicle!:GetVehicleDTO;
  originalVehicle!:GetVehicleDTO;
  constructor(private vehicleService: VehicleInfoService){}
  
  ngOnInit(){
    this.vehicleService.getVehicleInfo(4).subscribe({
      next: (vehicle) => {
        this.vehicle = vehicle;
        this.originalVehicle = structuredClone(vehicle);
      }
    })
  }

  saveChanges(){
    const dto: Vehicle = {
      model: this.vehicle.model,
      type: this.vehicle.type,
      seats: this.vehicle.seats,
      plateNumber: this.vehicle.plateNumber,
      babyTransport: this.vehicle.babyTransport,
      petFriendly: this.vehicle.petFriendly
    };

    this.vehicleService.updateVehicleInfo(4, dto).subscribe({
      next: (updatedVehicle: GetVehicleDTO) => {
        this.vehicle = updatedVehicle;
        this.originalVehicle = structuredClone(this.vehicle);
        alert("Changes saved successfully.");
      },
      error: (err) => {
        console.error('Update failed', err);
      }
    })
  }

  revertChanges(){
    this.vehicle = structuredClone(this.originalVehicle);
  }
}
