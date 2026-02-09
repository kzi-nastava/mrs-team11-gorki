import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreatedRouteDTO } from '../model/ui/created-route-dto';
import { environment } from '../../env/environment';

@Injectable({
  providedIn: 'root',
})
export class FavouriteRoutesService {
  constructor(private http:HttpClient){}

  getFavouriteRoutes(id:number): Observable<CreatedRouteDTO[]>{
    return this.http.get<any>(`${environment.apiHost}/passengers/${id}/favourite-routes`);
  }

  addFavouriteRoutes(id:number, routeId:number): Observable<CreatedRouteDTO>{
    return this.http.post<CreatedRouteDTO>(`${environment.apiHost}/passengers/${id}/favourite-routes/${routeId}`, {});
  }

  deleteFavouriteRoutes(id:number, routeId:number): Observable<void>{
    return this.http.delete<void>(`${environment.apiHost}/passengers/${id}/favourite-routes/${routeId}`);
  }
}
