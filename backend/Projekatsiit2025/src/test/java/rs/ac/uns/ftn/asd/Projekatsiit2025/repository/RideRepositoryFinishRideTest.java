package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class RideRepositoryFinishRideTest {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByIdAndDriver_Id_existingRideForDriver_returnsRide() {
        Driver driver = persistDriver("driver@test.com");
        Passenger passenger = persistPassenger("passenger@test.com");

        Ride ride = persistRide(driver, passenger, RideStatus.STARTED, LocalDateTime.now());

        Ride found = rideRepository.findByIdAndDriver_Id(ride.getId(), driver.getId());

        assertNotNull(found);
        assertEquals(ride.getId(), found.getId());
        assertEquals(driver.getId(), found.getDriver().getId());
    }

    @Test
    void findByIdAndDriver_Id_rideBelongsToAnotherDriver_returnsNull() {
        Driver driver = persistDriver("driver@test.com");
        Driver otherDriver = persistDriver("other.driver@test.com");
        Passenger passenger = persistPassenger("passenger@test.com");

        Ride ride = persistRide(driver, passenger, RideStatus.STARTED, LocalDateTime.now());

        Ride found = rideRepository.findByIdAndDriver_Id(ride.getId(), otherDriver.getId());

        assertNull(found);
    }

    @Test
    void findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc_returnsEarliestAcceptedRide() {
        Driver driver = persistDriver("driver@test.com");
        Driver otherDriver = persistDriver("other.driver@test.com");
        Passenger passenger = persistPassenger("passenger@test.com");

        LocalDateTime now = LocalDateTime.now();

        Ride laterAccepted = persistRide(driver, passenger, RideStatus.ACCEPTED, now.plusHours(3));
        Ride earliestAccepted = persistRide(driver, passenger, RideStatus.ACCEPTED, now.plusHours(1));
        persistRide(driver, passenger, RideStatus.STARTED, now.minusHours(1));
        persistRide(otherDriver, passenger, RideStatus.ACCEPTED, now.plusMinutes(10));

        Ride found = rideRepository.findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc(
                driver.getId(),
                RideStatus.ACCEPTED
        );

        assertNotNull(found);
        assertEquals(earliestAccepted.getId(), found.getId());
        assertNotEquals(laterAccepted.getId(), found.getId());
    }

    @Test
    void findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc_noAcceptedRide_returnsNull() {
        Driver driver = persistDriver("driver@test.com");
        Passenger passenger = persistPassenger("passenger@test.com");

        persistRide(driver, passenger, RideStatus.STARTED, LocalDateTime.now());

        Ride found = rideRepository.findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc(
                driver.getId(),
                RideStatus.ACCEPTED
        );

        assertNull(found);
    }

    private Driver persistDriver(String email) {
        Driver driver = new Driver();
        driver.setEmail(email);
        driver.setPassword("password");
        driver.setFirstName("Test");
        driver.setLastName("Driver");
        driver.setPhoneNumber(123456);
        driver.setAddress("Test address");
        driver.setActive(true);
        driver.setBlocked(false);
        driver.setStatus(DriverStatus.ACTIVE);

        return entityManager.persistAndFlush(driver);
    }

    private Passenger persistPassenger(String email) {
        Passenger passenger = new Passenger();
        passenger.setEmail(email);
        passenger.setPassword("password");
        passenger.setFirstName("Test");
        passenger.setLastName("Passenger");
        passenger.setPhoneNumber(654321);
        passenger.setAddress("Test address");
        passenger.setActive(true);
        passenger.setBlocked(false);
        passenger.setFavouriteRoutes(new ArrayList<>());

        return entityManager.persistAndFlush(passenger);
    }

    private Ride persistRide(
            Driver driver,
            Passenger passenger,
            RideStatus status,
            LocalDateTime scheduledTime
    ) {
        Ride ride = new Ride();
        ride.setDriver(driver);
        ride.setCreator(passenger);
        ride.setStatus(status);
        ride.setScheduledTime(scheduledTime);
        ride.setStartingTime(LocalDateTime.now());
        ride.setPaid(false);
        ride.setPanicActivated(false);
        ride.setLinkedPassengers(new ArrayList<>());

        return entityManager.persistAndFlush(ride);
    }
}