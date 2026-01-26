import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverScheduledRideCard } from './driver-scheduled-ride-card';

describe('DriverScheduledRideCard', () => {
  let component: DriverScheduledRideCard;
  let fixture: ComponentFixture<DriverScheduledRideCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverScheduledRideCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverScheduledRideCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
