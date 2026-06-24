package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.FinishRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.FinishedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.PassengerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RatingRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideServiceFinishRideTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverAssignmentService driverAssignmentService;

    @Mock
    private PriceConfigService priceConfigService;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    private RideService rideService;

    @BeforeEach
    void setUp() {
        rideService = new RideService(
                rideRepository,
                driverAssignmentService,
                priceConfigService,
                passengerRepository,
                ratingRepository,
                notificationService,
                emailService
        );
    }

    @Test
    void finishRide_validRideWithoutNextScheduledRide_finishesRideAndActivatesDriver() {
        Long driverId = 2L;
        Long rideId = 10L;

        Ride ride = createRide(rideId, driverId, "passenger@test.com");
        FinishRideDTO dto = createFinishRideDTO(rideId, true);

        when(rideRepository.findByIdAndDriver_Id(rideId, driverId)).thenReturn(ride);
        when(rideRepository.findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc(
                driverId, RideStatus.ACCEPTED
        )).thenReturn(null);

        FinishedRideDTO result = rideService.finishRide(driverId, dto);

        assertEquals(rideId, result.getRideId());
        assertEquals(RideStatus.FINISHED, result.getRideStatus());
        assertEquals(DriverStatus.ACTIVE, result.getDriverStatus());
        assertFalse(result.getHasNextScheduledRide());

        assertEquals(RideStatus.FINISHED, ride.getStatus());
        assertNotNull(ride.getEndingTime());
        assertTrue(ride.getPaid());
        assertEquals(DriverStatus.ACTIVE, ride.getDriver().getStatus());

        verify(rideRepository).save(ride);
        verify(notificationService).sendRatingAvailable("passenger@test.com", rideId);
        verify(notificationService).createAndSend(
                eq("passenger@test.com"),
                eq(rideId),
                eq("RIDE_FINISHED"),
                anyString()
        );
    }

    @Test
    void finishRide_validRideWithNextScheduledRide_finishesRideAndKeepsDriverBusy() {
        Long driverId = 2L;
        Long rideId = 10L;

        Ride currentRide = createRide(rideId, driverId, "passenger@test.com");
        Ride nextScheduledRide = createRide(11L, driverId, "passenger@test.com");

        FinishRideDTO dto = createFinishRideDTO(rideId, true);

        when(rideRepository.findByIdAndDriver_Id(rideId, driverId)).thenReturn(currentRide);
        when(rideRepository.findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc(
                driverId, RideStatus.ACCEPTED
        )).thenReturn(nextScheduledRide);

        FinishedRideDTO result = rideService.finishRide(driverId, dto);

        assertEquals(RideStatus.FINISHED, result.getRideStatus());
        assertEquals(DriverStatus.BUSY, result.getDriverStatus());
        assertTrue(result.getHasNextScheduledRide());

        assertEquals(RideStatus.FINISHED, currentRide.getStatus());
        assertEquals(DriverStatus.BUSY, currentRide.getDriver().getStatus());

        verify(rideRepository).save(currentRide);
    }

    @Test
    void finishRide_rideDoesNotExistOrBelongsToAnotherDriver_throwsException() {
        Long driverId = 2L;
        Long rideId = 10L;

        FinishRideDTO dto = createFinishRideDTO(rideId, true);

        when(rideRepository.findByIdAndDriver_Id(rideId, driverId)).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> rideService.finishRide(driverId, dto)
        );

        assertEquals("Voznja ne postoji ili nije dodeljena ovom vozacu", exception.getMessage());

        verify(rideRepository, never()).save(any());
        verifyNoInteractions(notificationService);
        verifyNoInteractions(emailService);
    }

    @Test
    void finishRide_paidFalse_keepsRideUnpaid() {
        Long driverId = 2L;
        Long rideId = 10L;

        Ride ride = createRide(rideId, driverId, "passenger@test.com");
        ride.setPaid(false);

        FinishRideDTO dto = createFinishRideDTO(rideId, false);

        when(rideRepository.findByIdAndDriver_Id(rideId, driverId)).thenReturn(ride);
        when(rideRepository.findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc(
                driverId, RideStatus.ACCEPTED
        )).thenReturn(null);

        FinishedRideDTO result = rideService.finishRide(driverId, dto);

        assertEquals(RideStatus.FINISHED, result.getRideStatus());
        assertFalse(ride.getPaid());
        verify(rideRepository).save(ride);
    }

    private FinishRideDTO createFinishRideDTO(Long rideId, Boolean paid) {
        FinishRideDTO dto = new FinishRideDTO();
        dto.setRideId(rideId);
        dto.setPaid(paid);
        return dto;
    }

    private Ride createRide(Long rideId, Long driverId, String passengerEmail) {
        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setEmail("driver@test.com");
        driver.setStatus(DriverStatus.BUSY);

        Passenger passenger = new Passenger();
        passenger.setId(99L);
        passenger.setEmail(passengerEmail);

        Ride ride = new Ride();
        ride.setId(rideId);
        ride.setDriver(driver);
        ride.setCreator(passenger);
        ride.setStatus(RideStatus.STARTED);
        ride.setPaid(false);

        ride.setLinkedPassengers(new ArrayList<>());

        return ride;
    }
}