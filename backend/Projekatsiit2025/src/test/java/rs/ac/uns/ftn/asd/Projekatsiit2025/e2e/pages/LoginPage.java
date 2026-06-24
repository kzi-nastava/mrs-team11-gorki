package rs.ac.uns.ftn.asd.Projekatsiit2025.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By openLoginButton = By.cssSelector("[data-testid='open-login-button']");
    private final By emailInput = By.cssSelector("[data-testid='login-email']");
    private final By passwordInput = By.cssSelector("[data-testid='login-password']");
    private final By submitLoginButton = By.cssSelector("[data-testid='login-submit-button']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open(String frontendUrl) {
        driver.get(frontendUrl + "/HomePage");
    }

    public void login(String email, String password) {
        WebElement openButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(openLoginButton)
        );

        clickWithJavaScript(openButton);

        WebElement emailElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailInput)
        );
        emailElement.clear();
        emailElement.sendKeys(email);

        WebElement passwordElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordInput)
        );
        passwordElement.clear();
        passwordElement.sendKeys(password);

        WebElement submitButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(submitLoginButton)
        );

        clickWithJavaScript(submitButton);

        wait.until(d -> {
            Object storageLength = ((JavascriptExecutor) d)
                    .executeScript("return localStorage.length > 0;");
            return Boolean.TRUE.equals(storageLength);
        });
    }

    private void clickWithJavaScript(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                element
        );
    }
}