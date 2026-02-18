import { TestBed } from '@angular/core/testing';

import { AdminRideMonitorService } from './admin-ride-monitor-service';

describe('AdminRideMonitorService', () => {
  let service: AdminRideMonitorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminRideMonitorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
