import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideEstimateUnuser } from './ride-estimate-unuser';

describe('RideEstimateUnuser', () => {
  let component: RideEstimateUnuser;
  let fixture: ComponentFixture<RideEstimateUnuser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideEstimateUnuser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideEstimateUnuser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
