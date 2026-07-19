package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

@DataJpaTest
public class RideRepositoryStopRideTest {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Test
    void findByIdAndDriverId_existingRide_returnsRide() {

        Driver driver = new Driver();
        driverRepository.save(driver);

        Ride ride = new Ride();

        ride.setDriver(driver);
        ride.setStatus(RideStatus.STARTED);
        ride.setPrice(1000.0);

        rideRepository.save(ride);

        Ride result =
                rideRepository.findByIdAndDriver_Id(
                        ride.getId(),
                        driver.getId()
                );


        assertNotNull(result);

        assertEquals(
                RideStatus.STARTED,
                result.getStatus()
        );
    }

    @Test
    void findByIdAndDriverId_wrongDriver_returnsNull() {


        Driver driver = new Driver();
        driverRepository.save(driver);

        Ride ride = new Ride();

        ride.setDriver(driver);
        ride.setStatus(RideStatus.STARTED);
        ride.setPrice(1000.0);


        rideRepository.save(ride);

        Ride result =
                rideRepository.findByIdAndDriver_Id(
                        ride.getId(),
                        999L
                );


        assertNull(result);

    }

    @Test
    void findFirstScheduledRide_returnsEarliestAcceptedRide() {


        Driver driver = new Driver();

        driverRepository.save(driver);

        Ride laterRide = new Ride();

        laterRide.setDriver(driver);
        laterRide.setStatus(RideStatus.ACCEPTED);
        laterRide.setPrice(1000.0);
        laterRide.setScheduledTime(
                LocalDateTime.now().plusHours(5)
        );

        Ride earlierRide = new Ride();

        earlierRide.setDriver(driver);
        earlierRide.setStatus(RideStatus.ACCEPTED);
        earlierRide.setPrice(1000.0);
        earlierRide.setScheduledTime(
                LocalDateTime.now().plusHours(1)
        );

        rideRepository.save(laterRide);
        rideRepository.save(earlierRide);

        Ride result =
                rideRepository
                .findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc(
                        driver.getId(),
                        RideStatus.ACCEPTED
                );



        assertNotNull(result);


        assertEquals(
                earlierRide.getScheduledTime(),
                result.getScheduledTime()
        );

    }

}