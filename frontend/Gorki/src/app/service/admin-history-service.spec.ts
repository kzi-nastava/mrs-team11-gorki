import { TestBed } from '@angular/core/testing';

import { AdminHistoryService } from './admin-history-service';

describe('AdminHistoryService', () => {
  let service: AdminHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
