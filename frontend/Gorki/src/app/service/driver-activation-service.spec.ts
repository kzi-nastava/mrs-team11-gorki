import { TestBed } from '@angular/core/testing';

import { DriverActivationService } from './driver-activation-service';

describe('DriverActivationService', () => {
  let service: DriverActivationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DriverActivationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
