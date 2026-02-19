import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../env/environment';
import { GetRouteDTO } from '../model/ui/get-route-dto';

@Injectable({
  providedIn: 'root',
})
export class FavouriteRoutesService {
  constructor(private http:HttpClient){}

  getFavouriteRoutes(id:number): Observable<GetRouteDTO[]>{
    return this.http.get<GetRouteDTO[]>(`${environment.apiHost}/passengers/${id}/favourite-routes`);
  }

  addFavouriteRoutes(id:number, rideId:number): Observable<GetRouteDTO>{
    return this.http.post<GetRouteDTO>(`${environment.apiHost}/passengers/${id}/favourite-routes/${rideId}`, {});
  }

  deleteFavouriteRoutes(id:number, routeId:number): Observable<void>{
    return this.http.delete<void>(`${environment.apiHost}/passengers/${id}/favourite-routes/${routeId}`);
  }
}
