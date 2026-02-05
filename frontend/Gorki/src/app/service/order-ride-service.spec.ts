import { TestBed } from '@angular/core/testing';

import { OrderRideService } from './order-ride-service';

describe('OrderRideService', () => {
  let service: OrderRideService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderRideService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
