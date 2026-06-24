import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { SupportChatService } from './chat-service';

describe('SupportChatService', () => {
  let service: SupportChatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(SupportChatService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});