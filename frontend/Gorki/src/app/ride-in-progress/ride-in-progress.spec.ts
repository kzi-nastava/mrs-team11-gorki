import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideInProgress } from './ride-in-progress';

describe('RideInProgress', () => {
  let component: RideInProgress;
  let fixture: ComponentFixture<RideInProgress>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideInProgress]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideInProgress);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
