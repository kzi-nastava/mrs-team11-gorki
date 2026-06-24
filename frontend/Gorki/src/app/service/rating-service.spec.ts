import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';

import { RatingService } from './rating-service';
import { environment } from '../../env/environment';
import { CreateRatingDto } from '../model/ui/create-rating-dto';
import { CreatedRatingDto } from '../model/ui/created-rating-dto';

describe('RatingService', () => {
  let service: RatingService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        RatingService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(RatingService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('rateRide should send POST request with entered rating data', () => {
    const rideId = 10;

    const dto: CreateRatingDto = {
      driverRating: 5,
      vehicleRating: 4,
      comment: 'Odlicna voznja',
    };

    const response: CreatedRatingDto = {
      ratingId: 1,
      rideId: 10,
      driverRating: 5,
      vehicleRating: 4,
      createdAt: '2026-01-01T10:00:00',
    };

    service.rateRide(rideId, dto).subscribe((res) => {
      expect(res).toEqual(response);
    });

    const req = httpMock.expectOne(
      `${environment.apiHost}/rides/${rideId}/rating`
    );

    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(dto);

    req.flush(response);
  });

  it('getPendingLatest should send GET request', () => {
    service.getPendingLatest().subscribe((res) => {
      expect(res).toBe(10);
    });

    const req = httpMock.expectOne(
      `${environment.apiHost}/rides/ratings/pending-latest`
    );

    expect(req.request.method).toBe('GET');

    req.flush(10);
  });
});