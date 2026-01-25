import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverScheduledRidesList } from './driver-scheduled-rides-list';

describe('DriverScheduledRidesList', () => {
  let component: DriverScheduledRidesList;
  let fixture: ComponentFixture<DriverScheduledRidesList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverScheduledRidesList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverScheduledRidesList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
