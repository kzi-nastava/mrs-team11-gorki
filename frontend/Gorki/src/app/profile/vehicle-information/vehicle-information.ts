import { Component } from '@angular/core';
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';

@Component({
  selector: 'app-vehicle-information',
  imports: [MatRadioModule, MatFormFieldModule, MatSelectModule],
  templateUrl: './vehicle-information.html',
  styleUrl: './vehicle-information.css',
})
export class VehicleInformation {

}
