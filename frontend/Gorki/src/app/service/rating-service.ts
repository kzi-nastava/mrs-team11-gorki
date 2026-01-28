import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../env/environment';
import { Observable } from 'rxjs';
import { CreateRatingDto } from '../model/ui/create-rating-dto';
import { CreatedRatingDto } from '../model/ui/created-rating-dto';
@Injectable({
  providedIn: 'root',
})
export class RatingService {
   constructor(private http: HttpClient) {}

  rateRide(rideId: number, dto: CreateRatingDto): Observable<CreatedRatingDto> {
    return this.http.post<CreatedRatingDto>(
      `${environment.apiHost}/rides/${rideId}/rating`,
      dto
    );
  }
}
