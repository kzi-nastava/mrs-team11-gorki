import { Injectable } from '@angular/core';
import * as L from 'leaflet';
import { MapComponent } from './map';

@Injectable({ providedIn: 'root' })
export class MapService {

  public map!: any;
  private mapComponent!: MapComponent;
  private pendingAction?: () => void;
  private lastRoutePoints?: [number, number][];

  private pendingRoute?: {
    from: [number, number];
    to: [number, number];
  };

  registerMap(map: L.Map, component: MapComponent) {
    this.map = map;
    this.mapComponent = component;

    if (this.pendingAction) {
      this.pendingAction();
      this.pendingAction = undefined;
    }
}

  async showSnappedRoute(
  from: [number, number],
  to: [number, number]
  ) {
    if (!this.mapComponent) {
      this.pendingRoute = { from, to };
      return;
    }

    const url =
      `https://router.project-osrm.org/route/v1/driving/` +
      `${from[1]},${from[0]};${to[1]},${to[0]}` +
      `?overview=full&geometries=geojson`;

    const res = await fetch(url);
    const data = await res.json();

    const coords: [number, number][] =
      data.routes[0].geometry.coordinates.map(
        (c: [number, number]) => [c[1], c[0]]
      );

    this.lastRoutePoints = coords;
    this.mapComponent.drawSnappedRoute(coords);
  }

  showInitialVehicles() {
    if (!this.mapComponent) {
      this.pendingAction = () => this.showInitialVehicles();
      return;
    }

    this.mapComponent.drawVehicles();
  }

  showCarAnimation(){
      if (!this.mapComponent || !this.lastRoutePoints) return;
      this.mapComponent.animateDriver(this.lastRoutePoints);
  }

  clearRoute() {
    this.mapComponent?.clearRoute();
  }

  clearVehicles(){
    this.mapComponent.clearVehicles();
    console.log("radim?");
  }
  
  clearAll() {
    this.mapComponent?.clearAll();
  }

  stopDriverAnimation(): [number, number] | null {
    return this.mapComponent?.stopDriverAnimation() ?? null;
  }


  setDriverToPanic(){
    this.mapComponent?.setDriverToPanic();
  }

  showDriverEndPosition(position:[number,number]){
      if (!this.mapComponent) {
      this.pendingAction = () => this.showDriverEndPosition(position);
      return;
    }

    this.mapComponent.drawDriverEndPosition(position);
  }
}
