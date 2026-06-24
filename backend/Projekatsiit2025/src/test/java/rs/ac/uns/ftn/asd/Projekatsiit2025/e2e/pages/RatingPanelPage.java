package rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RatingPanelPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By commentInput = By.cssSelector("[data-testid='rating-comment']");
    private final By submitButton = By.cssSelector("[data-testid='rating-submit-button']");
    private final By ratingPanel = By.cssSelector("[data-testid='rating-panel']");

    public RatingPanelPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void selectDriverRating(int rating) {
        By star = By.cssSelector("[data-testid='driver-star-" + rating + "']");
        wait.until(ExpectedConditions.elementToBeClickable(star)).click();
    }

    public void selectVehicleRating(int rating) {
        By star = By.cssSelector("[data-testid='vehicle-star-" + rating + "']");
        wait.until(ExpectedConditions.elementToBeClickable(star)).click();
    }

    public void enterComment(String comment) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(commentInput)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(commentInput)).sendKeys(comment);
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(ratingPanel));
    }

    public void rateRide(int driverRating, int vehicleRating, String comment) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ratingPanel));

        selectDriverRating(driverRating);
        selectVehicleRating(vehicleRating);
        enterComment(comment);
        submit();
    }

    public boolean isRatingPanelVisible() {
        return !driver.findElements(ratingPanel).isEmpty()
                && driver.findElement(ratingPanel).isDisplayed();
    }
}