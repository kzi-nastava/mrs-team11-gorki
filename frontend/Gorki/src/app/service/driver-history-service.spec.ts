import { TestBed } from '@angular/core/testing';

import { DriverHistoryService } from './driver-history-service';

describe('DriverHistoryService', () => {
  let service: DriverHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DriverHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
