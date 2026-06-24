package rs.ac.uns.ftn.asd.Projekatsiit2025.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseE2ETest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected final String frontendUrl =
            System.getProperty("e2e.frontend.url", "http://localhost:4200");

    @BeforeEach
    void setUpWebDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDownWebDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}