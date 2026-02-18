import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationInbox } from './notification-inbox';

describe('NotificationInbox', () => {
  let component: NotificationInbox;
  let fixture: ComponentFixture<NotificationInbox>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificationInbox]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificationInbox);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
