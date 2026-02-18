import {  AfterViewInit, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MapService } from './map-service';
import * as L from 'leaflet';
import { VehicleService } from '../service/vehicle-service';
import { HomeVehicle } from '../model/ui/vehicle';

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
var alertVehicle=L.icon({
          iconUrl:'alertVehicle.ico',
          iconSize:[74,60],
          iconAnchor:[30,30]
});

@Component({
  selector: 'app-map',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './map.html',
  styleUrls: ['./map.css']
})
export class MapComponent implements AfterViewInit {
  map!: any;
  private routeLayer = L.layerGroup();
  private vehiclesLayer = L.layerGroup();
  private markersLayer = L.layerGroup();
  private driverMarker?: L.Marker;

  private animationIndex = 0;
  private animationTimer: any;
  private animationPoints: [number, number][] = [];

  constructor(private mapService: MapService, private vehicleService:VehicleService) {}

  initMap(){
    this.map = L.map('map').setView([45.2671, 19.8335], 13);
    let DefaultIcon = L.icon({
          iconUrl: 'https://unpkg.com/leaflet@1.6.0/dist/images/marker-icon.png',
        });

    L.Marker.prototype.options.icon = DefaultIcon;

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);

    this.routeLayer.addTo(this.map);
    this.vehiclesLayer.addTo(this.map);
    this.markersLayer.addTo(this.map);
    this.mapService.registerMap(this.map, this);

  }

  ngAfterViewInit() {
    this.initMap();
  }

  animateDriver(points: [number, number][]) {
    if (!points || points.length === 0) return;

    this.animationPoints = points;
    this.animationIndex = 0;

    const driverIcon = L.icon({
      iconUrl: 'unavailableCar.ico',
      iconSize: [37, 30],
      iconAnchor: [18, 15]
    });

    if (!this.driverMarker) {
      this.driverMarker = L.marker(points[0], { icon: driverIcon });
      this.driverMarker.addTo(this.markersLayer);
    }

    const move = () => {
      if (this.animationIndex >= this.animationPoints.length) return;

      this.driverMarker!.setLatLng(
        this.animationPoints[this.animationIndex]
      );

      this.animationIndex++;

      this.animationTimer = setTimeout(() => {
        requestAnimationFrame(move);
      }, 200);
    };

    move();
  }

  drawSnappedRoute(points: [number, number][]) {
    this.routeLayer.clearLayers();
    this.markersLayer.clearLayers();

    const start = points[0];
    const end = points[points.length - 1];

    L.marker(start).bindPopup('Pickup').addTo(this.markersLayer);
    L.marker(end).bindPopup('Dropoff').addTo(this.markersLayer);

    const routeLine = L.polyline(points, {
      color: '#3B4EFF',
      weight: 7,
    }).addTo(this.routeLayer);

    this.map.fitBounds(routeLine.getBounds(), {
      padding: [60, 60]
    });
    
  }

  drawVehicles() {
    this.vehiclesLayer.clearLayers();

    this.vehicleService.getAllVehicles().subscribe({
      next: (allVehicles: HomeVehicle[]) => {
        allVehicles.forEach(v => {
          const lat = v.currentLocation.latitude;
          const lng = v.currentLocation.longitude;
        
          const icon = (v.vehicleAvailability === 'ACTIVE' || v.vehicleAvailability === 'BUSY')
            ? availableCar
            : unavailableCar;

          L.marker([lat, lng], { icon }).addTo(this.vehiclesLayer);
        });

      },
      error: (err) => {
        console.error('getAllVehicles failed', err);
      }
    });
}

  drawDriverEndPosition(position:[number,number]){
      L.marker(position, { icon: unavailableCar })
      .addTo(this.vehiclesLayer);
  }

  clearRoute() {
    this.routeLayer.clearLayers();
    this.markersLayer.clearLayers();
  }

  clearVehicles() {
    this.vehiclesLayer.clearLayers();
  }

  clearAll() {
    this.clearRoute();
    this.clearVehicles();
  }

  stopDriverAnimation(): [number, number] | null {
    if (this.animationTimer) {
      clearTimeout(this.animationTimer);
    }

    if (!this.driverMarker) return null;

    const pos = this.driverMarker.getLatLng();
    return [pos.lat, pos.lng];
  }

  setDriverToPanic() {
    if (!this.driverMarker) return;
    this.driverMarker.setIcon(alertVehicle);

  }

}
