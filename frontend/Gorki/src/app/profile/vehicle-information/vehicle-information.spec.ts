import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleInformation } from './vehicle-information';

describe('VehicleInformation', () => {
  let component: VehicleInformation;
  let fixture: ComponentFixture<VehicleInformation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehicleInformation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VehicleInformation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
