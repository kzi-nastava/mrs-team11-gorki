import { TestBed } from '@angular/core/testing';

import { PassengerRideInProgress } from './passenger-ride-in-progress';

describe('PassengerRideInProgress', () => {
  let service: PassengerRideInProgress;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PassengerRideInProgress);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
