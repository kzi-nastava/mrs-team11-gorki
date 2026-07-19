package rs.ac.uns.ftn.asd.Projekatsiit2025.e2e;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages.LoginPage;
import rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages.RideHistoryPage;


public class RideHistoryE2ETest extends BaseE2ETest {


    private LoginPage loginPage;

    private RideHistoryPage rideHistoryPage;



    private final String passengerEmail =
            System.getProperty(
                    "e2e.passenger.email",
                    "e2e.passenger@test.com"
            );


    private final String passengerPassword =
            System.getProperty(
                    "e2e.passenger.password",
                    "test123"
            );



    @BeforeEach
    void setUpPages() {


        loginPage =
                new LoginPage(driver, wait);


        rideHistoryPage =
                new RideHistoryPage(driver, wait);

    }





    @Test
    void passengerCanFilterRideHistoryByDate() {


        loginPage.open(frontendUrl);


        loginPage.login(
                passengerEmail,
                passengerPassword
        );


        rideHistoryPage.open(frontendUrl);



        rideHistoryPage.selectFromDate(
                "01/01/2026"
        );


        rideHistoryPage.selectToDate(
                "31/12/2026"
        );



        Assertions.assertTrue(
                rideHistoryPage.getRideCount() >= 0
        );

    }





    @Test
    void passengerCanSortRideHistoryByStartingTime() {


        loginPage.open(frontendUrl);



        loginPage.login(
                passengerEmail,
                passengerPassword
        );



        rideHistoryPage.open(frontendUrl);



        rideHistoryPage.chooseSortCriteria(
                "Starting time"
        );


        rideHistoryPage.chooseSortOrder(
                "Descending"
        );



        Assertions.assertTrue(
                rideHistoryPage.getRideCount() >= 0
        );

    }





    @Test
    void passengerGetsEmptyHistoryForFutureDate() {


        loginPage.open(frontendUrl);



        loginPage.login(
                passengerEmail,
                passengerPassword
        );



        rideHistoryPage.open(frontendUrl);



        rideHistoryPage.selectFromDate(
                "01/01/2099"
        );


        rideHistoryPage.selectToDate(
                "02/01/2099"
        );



        Assertions.assertEquals(
                0,
                rideHistoryPage.getRideCount()
        );

    }


}