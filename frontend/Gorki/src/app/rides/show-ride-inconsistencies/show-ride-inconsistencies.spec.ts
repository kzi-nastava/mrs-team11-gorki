import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowRideInconsistencies } from './show-ride-inconsistencies';

describe('ShowRideInconsistencies', () => {
  let component: ShowRideInconsistencies;
  let fixture: ComponentFixture<ShowRideInconsistencies>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowRideInconsistencies]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowRideInconsistencies);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
