package rs.ac.uns.ftn.asd.Projekatsiit2025.e2e;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages.LoginPage;
import rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages.RatingConfirmPanelPage;
import rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages.RatingPanelPage;

public class RatingE2ETest extends BaseE2ETest {

    private LoginPage loginPage;
    private RatingConfirmPanelPage ratingConfirmPanelPage;
    private RatingPanelPage ratingPanelPage;

    private final String passengerEmail =
            System.getProperty("e2e.passenger.email", "e2e.passenger@test.com");

    private final String passengerPassword =
            System.getProperty("e2e.passenger.password", "test123");

    @BeforeEach
    void setUpPages() {
        loginPage = new LoginPage(driver, wait);
        ratingConfirmPanelPage = new RatingConfirmPanelPage(driver, wait);
        ratingPanelPage = new RatingPanelPage(driver, wait);
    }

    @Test
    void passengerCanRateVehicleAndDriverFromConfirmationPanel() {
        loginPage.open(frontendUrl);
        loginPage.login(passengerEmail, passengerPassword);

        Assertions.assertTrue(
                ratingConfirmPanelPage.isVisible(),
                "Rating confirmation panel should be visible after login."
        );

        ratingConfirmPanelPage.clickRate();

        ratingPanelPage.rateRide(
                5,
                4,
                "E2E test comment for vehicle and driver rating"
        );

        Assertions.assertFalse(
                ratingPanelPage.isRatingPanelVisible(),
                "Rating panel should be closed after successful submit."
        );
    }

    @Test
    void passengerCanChooseLaterOnRatingConfirmationPanel() {
        loginPage.open(frontendUrl);
        loginPage.login(passengerEmail, passengerPassword);

        Assertions.assertTrue(
                ratingConfirmPanelPage.isVisible(),
                "Rating confirmation panel should be visible after login."
        );

        ratingConfirmPanelPage.clickLater();

        Assertions.assertFalse(
                ratingConfirmPanelPage.isVisible(),
                "Rating confirmation panel should be closed after clicking Later."
        );
    }
}