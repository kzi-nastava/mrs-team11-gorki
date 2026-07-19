package rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RideHistoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;


    private final By fromDate =
            By.id("dateInput1");


    private final By toDate =
            By.id("dateInput2");


    private final By sortCriteria =
            By.cssSelector("[data-testid='sort-criteria-select']");


    private final By sortOrder =
            By.cssSelector("[data-testid='sort-order-select']");


    private final By rideCards =
            By.cssSelector("app-ride-card-user");



    public RideHistoryPage(
            WebDriver driver,
            WebDriverWait wait
    ) {
        this.driver = driver;
        this.wait = wait;
    }



    public void open(String frontendUrl) {

        driver.get(
                frontendUrl + "/rides-list-user"
        );

    }



    public void selectFromDate(String date) {

        WebElement input =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(fromDate)
                );

        input.clear();
        input.sendKeys(date);
        input.sendKeys(Keys.ENTER);

    }



    public void selectToDate(String date) {

        WebElement input =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(toDate)
                );

        input.clear();
        input.sendKeys(date);
        input.sendKeys(Keys.ENTER);

    }



    public void chooseSortCriteria(String option) {

        wait.until(
                ExpectedConditions.elementToBeClickable(sortCriteria)
        ).click();


        By item =
                By.xpath(
                        "//mat-option//span[contains(text(),'"
                                + option +
                                "')]"
                );


        wait.until(
                ExpectedConditions.elementToBeClickable(item)
        ).click();

    }



    public void chooseSortOrder(String option) {

        wait.until(
                ExpectedConditions.elementToBeClickable(sortOrder)
        ).click();


        By item =
                By.xpath(
                        "//mat-option//span[contains(text(),'"
                                + option +
                                "')]"
                );


        wait.until(
                ExpectedConditions.elementToBeClickable(item)
        ).click();

    }



    public int getRideCount() {

        return driver.findElements(rideCards)
                .size();

    }

}