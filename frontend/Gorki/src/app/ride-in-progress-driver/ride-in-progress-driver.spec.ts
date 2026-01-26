import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideInProgressDriver } from './ride-in-progress-driver';

describe('RideInProgressDriver', () => {
  let component: RideInProgressDriver;
  let fixture: ComponentFixture<RideInProgressDriver>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideInProgressDriver]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideInProgressDriver);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
