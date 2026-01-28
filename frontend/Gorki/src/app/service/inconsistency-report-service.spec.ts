import { TestBed } from '@angular/core/testing';

import { InconsistencyReportService } from './inconsistency-report-service';

describe('InconsistencyReportService', () => {
  let service: InconsistencyReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InconsistencyReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
