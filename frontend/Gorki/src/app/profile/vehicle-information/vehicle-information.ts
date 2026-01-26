import { Component } from '@angular/core';
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';
import { Vehicle } from '../../model/ui/vehicle';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-vehicle-information',
  imports: [MatRadioModule, MatFormFieldModule, MatSelectModule, FormsModule],
  templateUrl: './vehicle-information.html',
  styleUrl: './vehicle-information.css',
})
export class VehicleInformation {
  vehicle: Vehicle = {
    model:'Ford Focus',
    type:'Standard',
    plateNumber:'NS-523-SV',
    seats:5,
    babyTransport:true,
    petFriendly:false,
  };
}
