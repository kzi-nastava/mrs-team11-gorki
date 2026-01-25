import {  AfterViewInit, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MapService } from './map-service';
import * as L from 'leaflet';

@Component({
  selector: 'app-map',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './map.html',
  styleUrls: ['./map.css']
})
export class MapComponent implements AfterViewInit {

  map!: any;
  private routeLayer?: L.Layer;

  constructor(private mapService: MapService) {}

  initMap(){
    this.map = L.map('map').setView([45.2671, 19.8335], 13);
    let DefaultIcon = L.icon({
          iconUrl: 'https://unpkg.com/leaflet@1.6.0/dist/images/marker-icon.png',
        });

    L.Marker.prototype.options.icon = DefaultIcon;

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);

    this.mapService.registerMap(this.map, this);

    var availableCar=L.icon({
          iconUrl:'availableCar.ico',
          iconSize:[37,30],
          iconAnchor:[15,15]
    });
    var unavailableCar=L.icon({
          iconUrl:'unavailableCar.ico',
          iconSize:[37,30],
          iconAnchor:[15,15]
    });

    L.marker([45.240055, 19.825589],{icon:availableCar}).addTo(this.map);
    L.marker([45.243599, 19.819983],{icon:unavailableCar}).addTo(this.map);
  }

  ngAfterViewInit() {
    this.initMap();
  }

  driverMarker?: L.Marker;
  animateDriver(points: [number, number][]) {
    if (!this.driverMarker) return;

    let i = 0;

    const move = () => {
      if (i >= points.length) return;

      this.driverMarker!.setLatLng(points[i]);
      i++;

      requestAnimationFrame(move);
    };
    setTimeout(move, 30); 
    move();
  }

  drawSnappedRoute(points: [number, number][]) {
    if (this.routeLayer) {
      this.routeLayer.remove();
    }
    
    const start = points[0];
    const end = points[points.length - 1];


    const startMarker = L.marker(start).bindPopup('Pickup');
    const endMarker = L.marker(end).bindPopup('Dropoff');

    const routeLine = L.polyline(points, {
      color: '#3B4EFF',
      weight: 7,
    });

    this.routeLayer = L.layerGroup([startMarker, endMarker, routeLine]).addTo(this.map);

    this.map.fitBounds(routeLine.getBounds(), {
      padding: [60, 60]
    });


  }

  drawVehicles(){
    //OVO JE PRIVREMENO OVDE
  }
  
}
