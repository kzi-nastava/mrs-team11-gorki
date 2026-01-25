import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideDetailsAdmin } from './ride-details-admin';

describe('RideDetailsAdmin', () => {
  let component: RideDetailsAdmin;
  let fixture: ComponentFixture<RideDetailsAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideDetailsAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideDetailsAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
