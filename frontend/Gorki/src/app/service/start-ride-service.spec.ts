import { TestBed } from '@angular/core/testing';

import { StartRideService } from './start-ride-service';

describe('StartRideService', () => {
  let service: StartRideService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StartRideService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
