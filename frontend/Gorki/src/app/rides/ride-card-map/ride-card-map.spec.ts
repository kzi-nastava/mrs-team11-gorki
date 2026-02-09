import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideCardMap } from './ride-card-map';

describe('RideCardMap', () => {
  let component: RideCardMap;
  let fixture: ComponentFixture<RideCardMap>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideCardMap]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideCardMap);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
