package rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserRideHistoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstEnabledRateButton = By.cssSelector(
            "[data-testid='rate-ride-button']:not([disabled])"
    );

    private final By ratingPanel = By.cssSelector("[data-testid='rating-panel']");

    public UserRideHistoryPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open(String frontendUrl) {
        driver.get(frontendUrl + "/rides-list-user");
    }

    public String getFirstRateableRideId() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(firstEnabledRateButton)
        );

        WebElement card = button.findElement(
                By.xpath("./ancestor::*[@data-testid='ride-card']")
        );

        return card.getDomAttribute("data-ride-id");
    }

    public void openRatingPanelForRide(String rideId) {
        By rateButtonForRide = By.cssSelector(
                "[data-testid='ride-card'][data-ride-id='" + rideId + "'] [data-testid='rate-ride-button']"
        );

        wait.until(ExpectedConditions.elementToBeClickable(rateButtonForRide)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(ratingPanel));
    }

    public String getRatingValueForRide(String rideId) {
        By ratingValue = By.cssSelector(
                "[data-testid='ride-card'][data-ride-id='" + rideId + "'] [data-testid='ride-rating-value']"
        );

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(ratingValue)
        ).getText().trim();
    }

    public boolean isRatingPanelVisible() {
        return !driver.findElements(ratingPanel).isEmpty()
                && driver.findElement(ratingPanel).isDisplayed();
    }

    public boolean isRateButtonDisabledForRide(String rideId) {
        By rateButtonForRide = By.cssSelector(
                "[data-testid='ride-card'][data-ride-id='" + rideId + "'] [data-testid='rate-ride-button']"
        );

        WebElement button = wait.until(
                ExpectedConditions.presenceOfElementLocated(rateButtonForRide)
        );

        return !button.isEnabled();
    }
}