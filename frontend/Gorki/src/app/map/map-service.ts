import { Injectable } from '@angular/core';
import * as L from 'leaflet';
import { MapComponent } from './map';

@Injectable({ providedIn: 'root' })
export class MapService {

  public map!: any;
  private mapComponent!: MapComponent;

  private pendingRoute?: {
    from: [number, number];
    to: [number, number];
  };

  registerMap(map: L.Map, component: MapComponent) {
    this.map = map;
    this.mapComponent = component;

    if (this.pendingRoute) {
      console.log(this.pendingRoute.from);
      console.log(this.pendingRoute.to);
      this.showSnappedRoute(
        this.pendingRoute.from,
        this.pendingRoute.to
      );
      this.pendingRoute = undefined;
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

    this.mapComponent.drawSnappedRoute(coords);
  }
}
