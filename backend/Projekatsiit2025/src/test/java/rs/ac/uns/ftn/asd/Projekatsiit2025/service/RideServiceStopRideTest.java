package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideStopResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.*;

@ExtendWith(MockitoExtension.class)
public class RideServiceStopRideTest {

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
    void stopRide_startedRide_finishesRideAndChangesPrice(){

        Long rideId = 1L;

        Ride ride = createStartedRide();

        when(rideRepository.findById(rideId))
                .thenReturn(Optional.of(ride));


        Location stopLocation =
                new Location(
                        45.267,
                        19.833,
                        "Novi Sad"
                );


        RideStopResponseDTO result =
                rideService.stopRide(
                        rideId,
                        stopLocation
                );


        assertEquals(
                RideStatus.FINISHED,
                ride.getStatus()
        );


        assertNotNull(
                ride.getEndingTime()
        );


        assertEquals(
                stopLocation,
                result.getStopAddress()
        );


        assertTrue(
                result.getPrice() >= 100
        );


        verify(rideRepository)
                .save(ride);
    }

    @Test
    void stopRide_rideDoesNotExist_throwsException(){

        when(rideRepository.findById(1L))
                .thenReturn(Optional.empty());


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> rideService.stopRide(
                                1L,
                                new Location()
                        )
                );


        assertEquals(
                "Ride not found",
                exception.getMessage()
        );


        verify(rideRepository, never())
                .save(any());
    }

    @Test
    void stopRide_notStartedRide_throwsException(){

        Ride ride = createStartedRide();

        ride.setStatus(RideStatus.ACCEPTED);


        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));


        assertThrows(
                RuntimeException.class,
                () -> rideService.stopRide(
                        1L,
                        new Location()
                )
        );


        verify(rideRepository, never())
                .save(any());
    }

    @Test
    void stopRide_missingTimes_throwsException(){

        Ride ride = createStartedRide();

        ride.setStartingTime(null);


        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));


        assertThrows(
                RuntimeException.class,
                () -> rideService.stopRide(
                        1L,
                        new Location()
                )
        );
    }

    @Test
    void stopRide_missingRoute_throwsException(){

        Ride ride = createStartedRide();

        ride.setRoute(null);


        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));


        assertThrows(
                RuntimeException.class,
                () -> rideService.stopRide(
                        1L,
                        new Location()
                )
        );
    }

    private Ride createStartedRide(){

        Ride ride = new Ride();

        ride.setId(1L);
        ride.setStatus(RideStatus.STARTED);

        ride.setPrice(1000.0);

        ride.setStartingTime(
                LocalDateTime.now()
                        .minusMinutes(10)
        );

        ride.setEndingTime(
                LocalDateTime.now()
                        .plusMinutes(20)
        );


        Route route = new Route();
        route.setLocations(
                new ArrayList<>()
        );


        ride.setRoute(route);


        return ride;
    }
}