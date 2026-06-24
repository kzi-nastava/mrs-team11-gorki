import { of, throwError } from 'rxjs';
import { vi } from 'vitest';

import { RidesListUser } from './rides-list-user';
import { UserHistoryRide } from '../models/ride';

describe('RidesListUser - rating', () => {
  let component: RidesListUser;

  let passengerHistoryService: any;
  let router: any;
  let ratingService: any;
  let authService: any;
  let cdr: any;

  beforeEach(() => {
    passengerHistoryService = {
      getUserRides: vi.fn().mockReturnValue(of([])),
    };

    router = {
      navigate: vi.fn(),
    };

    ratingService = {
      rateRide: vi.fn().mockReturnValue(of({})),
    };

    authService = {
      getId: vi.fn().mockReturnValue(1),
    };

    cdr = {
      detectChanges: vi.fn(),
    };

    component = new RidesListUser(
      passengerHistoryService,
      router,
      ratingService,
      authService,
      cdr
    );
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('openRatingPanel should select ride and show rating panel', () => {
    const ride = createRide({ id: 10 });

    component.openRatingPanel(ride);

    expect(component.selectedRideForRating).toEqual(ride);
    expect(component.showRating).toBe(true);
  });

  it('handleRating should send entered data to RatingService', () => {
    const ride = createRide({ id: 10, rating: 0 });

    component.rides = [ride];
    component.selectedRideForRating = ride;
    component.showRating = true;

    component.handleRating({
      driverRating: 5,
      vehicleRating: 4,
      comment: 'Odlicna voznja',
    });

    expect(ratingService.rateRide).toHaveBeenCalledWith(10, {
      driverRating: 5,
      vehicleRating: 4,
      comment: 'Odlicna voznja',
    });
  });

  it('handleRating should update ride rating after successful rating', () => {
    const ride = createRide({ id: 10, rating: 0 });

    component.rides = [ride];
    component.selectedRideForRating = ride;
    component.showRating = true;

    component.handleRating({
      driverRating: 5,
      vehicleRating: 4,
      comment: 'Odlicna voznja',
    });

    expect(component.rides[0].rating).toBe(4.5);
    expect(component.showRating).toBe(false);
    expect(component.selectedRideForRating).toBeNull();
    expect(cdr.detectChanges).toHaveBeenCalled();
  });

  it('handleRating should not call RatingService when no ride is selected', () => {
    component.selectedRideForRating = null;

    component.handleRating({
      driverRating: 5,
      vehicleRating: 4,
      comment: 'Komentar',
    });

    expect(ratingService.rateRide).not.toHaveBeenCalled();
  });

  it('handleRating should keep rating panel open when service returns error', () => {
    const ride = createRide({ id: 10, rating: 0 });

    ratingService.rateRide.mockReturnValue(
      throwError(() => new Error('Server error'))
    );

    const alertSpy = vi
      .spyOn(window, 'alert')
      .mockImplementation(() => undefined);

    const consoleSpy = vi
      .spyOn(console, 'error')
      .mockImplementation(() => undefined);

    component.rides = [ride];
    component.selectedRideForRating = ride;
    component.showRating = true;

    component.handleRating({
      driverRating: 5,
      vehicleRating: 4,
      comment: 'Komentar',
    });

    expect(ratingService.rateRide).toHaveBeenCalledWith(10, {
      driverRating: 5,
      vehicleRating: 4,
      comment: 'Komentar',
    });

    expect(component.showRating).toBe(true);
    expect(component.selectedRideForRating).toEqual(ride);
    expect(component.rides[0].rating).toBe(0);
    expect(alertSpy).toHaveBeenCalledWith('Rating nije uspeo');

    alertSpy.mockRestore();
    consoleSpy.mockRestore();
  });

  function createRide(overrides: Partial<UserHistoryRide> = {}): UserHistoryRide {
    return {
      id: 1,
      rating: 0,
      startTime: '10:00',
      endTime: '10:20',
      startLocation: 'Start lokacija',
      destination: 'Destinacija',
      date: new Date(),
      price: 1000,
      canceled: false,
      canceledBy: 'None',
      cancellationReason: 'None',
      panic: false,
      passengers: [],
      ...overrides,
    };
  }
});