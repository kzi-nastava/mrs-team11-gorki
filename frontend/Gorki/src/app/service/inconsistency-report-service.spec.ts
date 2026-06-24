import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { InconsistencyService } from './inconsistency-report-service';

describe('InconsistencyService', () => {
  let service: InconsistencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(InconsistencyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});