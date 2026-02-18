import { TestBed } from '@angular/core/testing';

import { PanicSocketService } from './panic-socket-service';

describe('PanicSocketService', () => {
  let service: PanicSocketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PanicSocketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
