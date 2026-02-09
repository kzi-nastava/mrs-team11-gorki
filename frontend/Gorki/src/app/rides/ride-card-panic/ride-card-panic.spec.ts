import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideCardPanic } from './ride-card-panic';

describe('RideCardPanic', () => {
  let component: RideCardPanic;
  let fixture: ComponentFixture<RideCardPanic>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideCardPanic]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideCardPanic);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
