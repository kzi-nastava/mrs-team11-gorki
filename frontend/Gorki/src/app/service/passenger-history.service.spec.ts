import { TestBed } from '@angular/core/testing';

import { PassengerHistoryService } from './passenger-history-service';

describe('PassengerHistoryService', () => {
  let service: PassengerHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PassengerHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
