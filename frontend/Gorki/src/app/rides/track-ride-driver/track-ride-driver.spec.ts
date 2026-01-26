import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackRideDriver } from './track-ride-driver';

describe('TrackRideDriver', () => {
  let component: TrackRideDriver;
  let fixture: ComponentFixture<TrackRideDriver>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackRideDriver]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrackRideDriver);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
