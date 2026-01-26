import { Component } from '@angular/core';
import { Map } from "../../map/map";
import { RideOrdering } from '../../ride-ordering/ride-ordering';
import { RideStart } from '../../ride-start/ride-start';

@Component({
  selector: 'app-home',
  imports: [Map, RideOrdering, RideStart],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  role: string = "user"
}
