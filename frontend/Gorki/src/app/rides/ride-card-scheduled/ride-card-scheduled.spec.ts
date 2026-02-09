import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideCardScheduled } from './ride-card-scheduled';

describe('RideCardScheduled', () => {
  let component: RideCardScheduled;
  let fixture: ComponentFixture<RideCardScheduled>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideCardScheduled]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideCardScheduled);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
