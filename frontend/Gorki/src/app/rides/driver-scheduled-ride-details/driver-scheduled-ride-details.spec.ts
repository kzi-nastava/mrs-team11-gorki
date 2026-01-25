import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverScheduledRideDetails } from './driver-scheduled-ride-details';

describe('DriverScheduledRideDetails', () => {
  let component: DriverScheduledRideDetails;
  let fixture: ComponentFixture<DriverScheduledRideDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverScheduledRideDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverScheduledRideDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
