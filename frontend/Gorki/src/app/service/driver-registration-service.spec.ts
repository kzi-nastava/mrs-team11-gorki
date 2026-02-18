import { TestBed } from '@angular/core/testing';

import { DriverRegistrationService } from './driver-registration-service';

describe('DriverRegistrationService', () => {
  let service: DriverRegistrationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DriverRegistrationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
