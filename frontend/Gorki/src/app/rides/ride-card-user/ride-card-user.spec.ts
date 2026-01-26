import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideCardUser } from './ride-card-user';

describe('RideCardUser', () => {
  let component: RideCardUser;
  let fixture: ComponentFixture<RideCardUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideCardUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideCardUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
