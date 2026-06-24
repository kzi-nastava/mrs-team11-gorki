package rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class RatingConfirmPanelPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By rateButton = By.cssSelector("[data-testid='rating-confirm-rate-button']");
    private final By laterButton = By.cssSelector("[data-testid='rating-confirm-later-button']");

    public RatingConfirmPanelPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isVisible() {
        return !driver.findElements(rateButton).isEmpty()
                && driver.findElement(rateButton).isDisplayed();
    }

    public void clickRate() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(rateButton)
        );

        button.click();
    }

    public void clickLater() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(laterButton)
        );

        button.click();
    }
}