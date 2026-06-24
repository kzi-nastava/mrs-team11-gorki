import { ComponentFixture, TestBed } from '@angular/core/testing';
import { vi } from 'vitest';

import { RatingPanel } from './rating-panel';

describe('RatingPanel', () => {
  let component: RatingPanel;
  let fixture: ComponentFixture<RatingPanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RatingPanel],
    }).compileComponents();

    fixture = TestBed.createComponent(RatingPanel);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('setDriverRating should set selected driver rating', () => {
    component.setDriverRating(5);

    expect(component.driverRating).toBe(5);
  });

  it('setVehicleRating should set selected vehicle rating', () => {
    component.setVehicleRating(4);

    expect(component.vehicleRating).toBe(4);
  });

  it('onSubmit should emit entered rating data', () => {
    const submitSpy = vi.fn();

    component.submit.subscribe(submitSpy);

    component.driverRating = 5;
    component.vehicleRating = 4;
    component.comment = 'Sve je bilo odlicno';

    component.onSubmit();

    expect(submitSpy).toHaveBeenCalledWith({
      driverRating: 5,
      vehicleRating: 4,
      comment: 'Sve je bilo odlicno',
    });
  });

  it('onSubmit should emit close event after submit', () => {
    const closeSpy = vi.fn();

    component.close.subscribe(closeSpy);

    component.driverRating = 5;
    component.vehicleRating = 4;
    component.comment = 'Komentar';

    component.onSubmit();

    expect(closeSpy).toHaveBeenCalled();
  });
});