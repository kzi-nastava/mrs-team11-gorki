import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { RideInProgressService } from './passenger-ride-in-progress';

describe('RideInProgressService', () => {
  let service: RideInProgressService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(RideInProgressService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});