package hk1823.automation.Utility.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class Selenium1823FormTest {
    static WebDriver driver;

    @BeforeAll
    static void setup() {
        driver = new ChromeDriver();
    }

    @AfterAll
    static void teardown() {
        driver.quit();
    }

    @Test
    void testFeedbackForm() {
        driver.get("https://www.1823.gov.hk/tc");

        try {
            WebElement feedbackBtn = driver.findElement(By.linkText("意見反映")); // Example: Feedback link
            feedbackBtn.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("textarea")));
            textarea.sendKeys("這是一個自動化測試。");

            WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
            submitBtn.click();

            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("success-message")));
            Assertions.assertTrue(successMsg.getText().contains("成功") || successMsg.getText().contains("已收到"));
        } catch (NoSuchElementException e) {
            Assertions.fail("Could not find feedback form elements. Update selectors as needed.");
        }
    }
} 