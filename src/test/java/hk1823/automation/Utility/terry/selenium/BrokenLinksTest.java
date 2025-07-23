package hk1823.automation.Utility.terry.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.net.HttpURLConnection;
import java.net.URL;

public class BrokenLinksTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String MAIN_MENU_URL = "https://www.1823.gov.hk/en/faq/service-categories";
    private static final Duration ELEMENT_TIMEOUT = Duration.ofSeconds(10);
    private int faqCounter = 0; // Counter to track number of FAQs processed

    //@BeforeEach
    public void setUp() {
        // Set up ChromeDriver path and initialize WebDriver
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "\\src\\main\\resources\\driver\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, ELEMENT_TIMEOUT);
        driver.manage().window().maximize(); // Maximize browser window
    }

    //@AfterEach
    public void tearDown() {
        // Clean up - close the browser after each test
        if (driver != null) {
            driver.quit();
        }
    }

    //@Test
    public void testScraping() {
        try {
            // Navigate to the main FAQ categories page
            driver.get(MAIN_MENU_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.menu__link.menu__link--lv1")));

            // Get all top-level menu items
            List<WebElement> menuItems = getMenuItems();
            System.out.println("Found " + menuItems.size() + " menu items.");

            // Process each menu item starting from index 5 (skipping first few items)
            for (int i = 5; i < menuItems.size(); i++) {
                String mainWindow = driver.getWindowHandle(); // Remember main window
                try {
                    WebElement menuItem = wait.until(ExpectedConditions.elementToBeClickable(menuItems.get(i)));
                    String menuText = getMenuText(menuItem);
                    System.out.println("\n==== Processing Menu Item #" + (i - 4) + ": " + menuText + " ====");
                    processMenuItem(menuItem, mainWindow);
                } catch (StaleElementReferenceException e) {
                    // If element becomes stale, refresh and retry
                    System.out.println("Stale element, refreshing menu items...");
                    driver.navigate().refresh();
                    menuItems = getMenuItems();
                    i--; // Retry same index
                    continue;
                }

                // Return to main window after processing each menu item
                driver.switchTo().window(mainWindow);
                waitForPageReady();
            }
        } catch (Exception e) {
            System.err.println("Scraping failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Extracts text from menu item, trying image alt text first, then falling back to element text
    private String getMenuText(WebElement menuItem) {
        try {
            WebElement img = menuItem.findElement(By.cssSelector("figure.placeholder img"));
            String altText = img.getAttribute("alt");
            if (altText != null && !altText.trim().isEmpty()) {
                return altText;
            }
        } catch (NoSuchElementException e) {
            // Image not found, proceed to fallback
        }
        return menuItem.getText();
    }

    // Processes an individual menu item (category)
    private void processMenuItem(WebElement menuItem, String mainWindow) {
        String menuUrl = menuItem.getAttribute("href");
        String menuText = getMenuText(menuItem);
        
        if (!isValidUrl(menuUrl)) {
            System.out.println("Skipping invalid menu URL");
            return;
        }

        System.out.println("Menu Item: " + menuText);
        System.out.println("Menu URL: " + menuUrl);

        // Open menu item in new tab and process its FAQs
        String menuTab = openNewTab(menuUrl);
        try {
            driver.switchTo().window(menuTab);
            waitForPageReady();
            processAllFaqPages(menuTab, mainWindow); // Process all FAQs in this category
        } finally {
            safeCloseTab(menuTab, mainWindow); // Ensure tab is closed even if errors occur
        }
    }

    // Processes all FAQ pages within a category (handles pagination)
    private void processAllFaqPages(String parentTab, String mainWindow) {
        int currentPage = 1;
        boolean hasNextPage;
        
        do {
            System.out.println("\nProcessing page " + currentPage);
            processFaqsOnCurrentPage(parentTab, mainWindow); // Process all FAQs on current page
            
            // Check for next page
            hasNextPage = false;
            try {
                WebElement nextPageLink = driver.findElement(By.cssSelector("li.pagination__item a.pagination__link[href*='page" + (currentPage + 1) + "']"));
                if (nextPageLink.isDisplayed()) {
                    String nextPageUrl = nextPageLink.getAttribute("href");
                    System.out.println("Found next page: " + nextPageUrl);
                    driver.get(nextPageUrl); // Navigate to next page
                    waitForPageReady();
                    currentPage++;
                    hasNextPage = true;
                }
            } catch (NoSuchElementException e) {
                // No more pages
            } catch (StaleElementReferenceException e) {
                // Element became stale, try again
                hasNextPage = true;
            }
        } while (hasNextPage); // Continue until no more pages
    }

    // Processes all FAQs on the current page
    private void processFaqsOnCurrentPage(String parentTab, String mainWindow) {
        List<WebElement> faqLinks;
        try {
            // Find all FAQ links on the page
            faqLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("a.icon-desc.icon-desc--detail")));
        } catch (TimeoutException e) {
            System.out.println("No FAQs found on this page");
            return;
        }

        System.out.println("  Found " + faqLinks.size() + " FAQs on this page");
        for (WebElement faqLink : faqLinks) {
            faqCounter++;
            String faqUrl = faqLink.getAttribute("href");
            String faqText = faqLink.getText();
            if (!isValidUrl(faqUrl)) {
                System.out.println("  Skipping invalid FAQ URL");
                continue;
            }

            System.out.println("  FAQ #" + faqCounter + ": " + faqText);
            System.out.println("  FAQ URL: " + faqUrl);

            // Open FAQ in new tab and process its links
            String faqTab = openNewTab(faqUrl);
            try {
                driver.switchTo().window(faqTab);
                waitForPageReady();
                processAllLinksInFaq(); // Process all links within this FAQ
            } finally {
                safeCloseTab(faqTab, parentTab); // Ensure FAQ tab is closed
            }
        }
    }

    // Processes all links within a single FAQ page
    private void processAllLinksInFaq() {
        try {
            // Find the main content area of the FAQ
            WebElement contentDiv = findContentDiv();
            if (contentDiv == null) {
                System.out.println("    No content div found");
                return;
            }
            
            // Get all links in the content area
            List<WebElement> links = contentDiv.findElements(By.cssSelector("a[href]"));
            System.out.println("    Found " + links.size() + " links in content area");
            
            String faqWindow = driver.getWindowHandle(); // Remember FAQ window
            
            // Process each link
            for (int i = 0; i < links.size(); i++) {
                try {
                    // Re-find elements to avoid staleness
                    contentDiv = findContentDiv();
                    links = contentDiv.findElements(By.cssSelector("a[href]"));
                    if (i >= links.size()) break;
                    
                    WebElement link = links.get(i);
                    String linkUrl = link.getAttribute("href");
                    String linkText = link.getText();

                    if (!isValidUrl(linkUrl)) {
                        System.out.println("    Skipping invalid link: " + linkUrl);
                        continue;
                    }
                    
                    // Skip email links
                    if (linkUrl.startsWith("mailto:")) {
                        System.out.println("    Found email link (skipped): " + linkText + " (" + linkUrl + ")");
                        continue;
                    }

                    // Handle file download links differently
                    if (isFileDownloadLink(linkUrl)) {
                        String fileName = extractFileName(linkUrl);
                        System.out.println("    File Download: " + fileName);
                        System.out.println("    Download URL: " + linkUrl);
                        continue;
                    }

                    System.out.println("    Link Text: " + linkText);
                    System.out.println("    Link URL: " + linkUrl);
                    
                    // Check if link is broken (HTTP status)
                    checkLinkStatus(linkUrl);
                    
                    // Optional: Open link in new tab for visual verification
                    String linkTab = openNewTab(linkUrl);
                    try {
                        driver.switchTo().window(linkTab);
                        waitForPageReady();
                    } finally {
                        driver.close();
                        driver.switchTo().window(faqWindow);
                    }
                } catch (StaleElementReferenceException e) {
                    i--; // Retry same index if element became stale
                } catch (Exception e) {
                    System.out.println("    Error processing link: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("    Error processing links: " + e.getMessage());
        }
    }

    // Checks HTTP status of a URL to detect broken links
    private void checkLinkStatus(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD"); // Use HEAD for faster checking
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.setReadTimeout(5000);
            connection.connect();
            
            int responseCode = connection.getResponseCode();
            if (responseCode >= 400) {
                System.out.println("    BROKEN LINK: HTTP " + responseCode + " - " + url);
            } else {
                System.out.println("    Link OK: HTTP " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("    ERROR checking link: " + e.getMessage() + " - " + url);
        }
    }

    // Checks if URL points to a file download
    private boolean isFileDownloadLink(String url) {
        String[] fileExtensions = {".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".zip", ".rar"};
        for (String ext : fileExtensions) {
            if (url.toLowerCase().contains(ext)) {
                return true;
            }
        }
        return false;
    }

    // Extracts filename from URL
    private String extractFileName(String url) {
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash != -1 && lastSlash < url.length() - 1) {
            return url.substring(lastSlash + 1);
        }
        return url;
    }

    // Finds the main content div using multiple possible selectors
    private WebElement findContentDiv() {
        String[] selectors = {
            "div.ckec", 
            "div.main-content",
            "div.content",
            "div.article-body",
            "section.main",
            "article"
        };
        
        // Try each selector until one works
        for (String selector : selectors) {
            try {
                return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector)));
            } catch (TimeoutException e) {
                continue;
            }
        }
        return null;
    }

    // Opens URL in new tab and returns the new tab handle
    private String openNewTab(String url) {
        Set<String> originalHandles = driver.getWindowHandles();
        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", url);
        
        // Wait for new tab to open and return its handle
        return wait.until(d -> {
            Set<String> newHandles = driver.getWindowHandles();
            newHandles.removeAll(originalHandles);
            return newHandles.isEmpty() ? null : newHandles.iterator().next();
        });
    }

    // Safely closes a tab and switches back to fallback window
    private void safeCloseTab(String tabToClose, String fallbackWindow) {
        try {
            if (driver.getWindowHandles().contains(tabToClose)) {
                driver.switchTo().window(tabToClose);
                driver.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing tab: " + e.getMessage());
        }
        driver.switchTo().window(fallbackWindow);
    }

    // Gets all top-level menu items
    private List<WebElement> getMenuItems() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.cssSelector("a.menu__link.menu__link--lv1")));
    }

    // Validates if URL is worth processing
    private boolean isValidUrl(String url) {
        return url != null 
               && !url.trim().isEmpty() 
               && !url.equals("#") 
               && !url.startsWith("javascript:")
               && !url.startsWith("mailto:");
    }

    // Waits for page to be fully loaded
    private void waitForPageReady() {
        wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
        waitForPageLoad(500); // Additional small delay for stability
    }

    // Simple delay
    private void waitForPageLoad(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}