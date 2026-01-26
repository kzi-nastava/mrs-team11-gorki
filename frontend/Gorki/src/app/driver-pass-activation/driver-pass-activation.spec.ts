import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverPassActivation } from './driver-pass-activation';

describe('DriverPassActivation', () => {
  let component: DriverPassActivation;
  let fixture: ComponentFixture<DriverPassActivation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverPassActivation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverPassActivation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
