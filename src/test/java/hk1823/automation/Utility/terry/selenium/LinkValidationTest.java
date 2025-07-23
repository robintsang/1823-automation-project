package hk1823.automation.Utility.terry.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class LinkValidationTest {
    private static WebDriver driver;

    //@BeforeAll
    static void setUp() {
        // Chrome Driver Setup
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "\\src\\main\\resources\\driver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize(); // Maximize window for better visibility
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10)); // Set page load timeout
        driver.get("https://www.1823.gov.hk/en");
    }

    //@Test
    public void testHotSearchItems() {
        By hotSearchItemsLocator = By.cssSelector(".container--search .hot-search__body a.hot-search__item"); // Locator for hot search items
        List<WebElement> hotSearchItems = driver.findElements(hotSearchItemsLocator); // Get all hot search items
        
        String mainWindowHandle = driver.getWindowHandle(); // Store current window handle for navigation back
        
        for (WebElement item : hotSearchItems) {
            try {
                // Get item details before interaction
                String itemText = item.getText().trim();
                String itemHref = item.getAttribute("href");
                System.out.println("Testing: " + itemText);
                System.out.println(" (Link: " + itemHref + ")");

                // Open link in new tab to avoid navigation issues
                ((JavascriptExecutor)driver).executeScript("window.open(arguments[0])", itemHref);
                
                // Switch to new tab
                for (String handle : driver.getWindowHandles()) {
                    if (!handle.equals(mainWindowHandle)) {
                        driver.switchTo().window(handle);
                        break;
                    }
                }
                
                // Simple verification - check if page contains expected elements

                if (driver.getTitle().contains("Page Not Found 404")) {
                    System.err.println("\n404 error found for: " + itemText + "\n");
                }

                // Close current tab and switch back to main window
                driver.close();
                driver.switchTo().window(mainWindowHandle);

            } catch (NoSuchElementException | TimeoutException e) {
                // Handle errors for individual items without failing entire test
                System.err.println("Error testing item: " + e.getMessage());
                // Ensure we're back on the main window for next iteration
                if (!driver.getWindowHandle().equals(mainWindowHandle)) {
                    driver.close();
                    driver.switchTo().window(mainWindowHandle);
                }
            }
        }
    }

    //@AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}