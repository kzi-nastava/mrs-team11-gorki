import { TestBed } from '@angular/core/testing';

import { PriceConfigService } from './price-config-service';

describe('PriceConfigService', () => {
  let service: PriceConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PriceConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
