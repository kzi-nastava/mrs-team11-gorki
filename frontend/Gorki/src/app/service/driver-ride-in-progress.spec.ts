import { TestBed } from '@angular/core/testing';

import { DriverRideInProgress } from './driver-ride-in-progress';

describe('DriverRideInProgress', () => {
  let service: DriverRideInProgress;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DriverRideInProgress);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
