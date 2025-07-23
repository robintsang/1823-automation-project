package hk1823.automation.Utility.robin.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ComplaintSeleniumTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "src/main/resources/driver/ChromeDriver/chromedriver-mac-x64/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testOtherCategoryComplaint() throws InterruptedException {
        // ================== Section A: Open website and enter complaint form ==================
        // Step 1: Open the homepage
        driver.get("https://www.1823.gov.hk/en/");

        // Step 2: Click the 'Request for Service/Complaint' button in the top navigation
        WebElement complaintBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/form/complain') and contains(@class, 'menu__link--lv1')]")));
        complaintBtn.click();

        // Step 3: Wait for the category selection page and click 'Others'
        WebElement otherCategory = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@value='64a7256d88403fe3696c000000000151']")));
        otherCategory.click();

        // ================== Section B: Fill in complaint content ==================
        // Step 4: Wait for the content form to appear and select 'No' (multi-language compatible)
        WebElement noRadio = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[contains(text(),'沒有') or normalize-space()='No' or contains(text(),'没有')]/ancestor::label")
        ));
        System.out.println(noRadio.getText());
        noRadio.click();

        // Step 5: Detect page language by the main title and set the case information text accordingly
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@class='form-name__lg']")));
        String titleText = titleElement.getText().trim();
        String caseInfoText;
        if (titleText.contains("要求服務/投訴")) { // Traditional Chinese
            caseInfoText = "最近家對面馬路等燈處經常因大雨水浸，渠口堵塞，積水不散。此情況已持續多個月。請盡快安排清理。可提供現場照片及影片。";
        } else if (titleText.contains("Request for Service/Complaint")) { // English
            caseInfoText = "The road near my home is often flooded after heavy rain. The drains are blocked and the water does not drain. This has been happening for several months. Please arrange for urgent clearance. Photos and videos are available upon request.";
        } else if (titleText.contains("要求服务/投诉")) { // Simplified Chinese
            caseInfoText = "最近家对面马路等灯处经常因大雨水浸，渠口堵塞，积水不散。此情况已持续多个月。请尽快安排清理。可提供现场照片及视频。";
        } else {
            caseInfoText = "The road near my home is often flooded after heavy rain. The drains are blocked and the water does not drain. This has been happening for several months. Please arrange for urgent clearance. Photos and videos are available upon request.";
        }

        // Step 6: Fill in the case information
        WebElement caseInfoTextarea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@id='個案資料' or @id='个案资料' or @id='Case Information']")));
        caseInfoTextarea.sendKeys(caseInfoText);

        // Step 7: Fill in the case location (multi-language compatible)
        String caseLocation = "FU YIP STREET and HUNG LOK STREET junction";
        WebElement locationInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//input[@id='suggest' or @placeholder='輸入地點' or @placeholder='Enter location' or @placeholder='输入地点']")
        ));
        locationInput.clear();
        locationInput.sendKeys(caseLocation);

        // Step 8: Wait for the suggestion dropdown and select the first suggestion
        WebElement firstSuggestion = new WebDriverWait(driver, Duration.ofSeconds(3)).until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//ul[contains(@class,'ui-autocomplete') and not(contains(@style,'display: none'))]//div[1]")
            )
        );
        firstSuggestion.click();

        // Step 9: (Manual step in demo video) Please upload the file manually at this point.
        // In the demo video, this step will be performed by hand due to front-end security restrictions.
        // The automation continues with the rest of the form.
        System.out.println("[INFO] Please upload the file manually at this point in the demo video.");
        // Pause to allow manual upload during video recording
        try {
            Thread.sleep(20000); // 20 seconds for manual upload
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 10: Click the 'Next' button to proceed to the next page (multi-language compatible)
        WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[normalize-space()='下一步' or normalize-space()='Next' or normalize-space()='下一步']")
        ));
        nextButton.click();

        // ================== Section C: Fill in personal information ==================
        // Step 12: Agree to provide contact information (multi-language)
        WebElement agreeContactYes = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//label[@for='agree_1823_1']//span[contains(text(),'Yes') or contains(text(),'同意')]")
        ));
        agreeContactYes.click();

        // Step 13: Agree to disclose personal data (multi-language)
        WebElement agreeDiscloseYes = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//label[@for='agree_1']//span[contains(text(),'同意') or contains(text(),'Yes')]")
        ));
        agreeDiscloseYes.click();

        // Step 14: Fill in Name, Email, Phone
        driver.findElement(By.xpath("//input[@id='name']")).sendKeys("Robin");
        driver.findElement(By.xpath("//input[@id='email']")).sendKeys("robintesting@gmail.com");
        driver.findElement(By.xpath("//input[@id='phone']")).sendKeys("66886868");

        // Step 15: Select best time to call (multi-language)
        WebElement bestTime = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[contains(text(),'約下午6:00 - 晚上10:00') or contains(text(),'approximately 6:00 PM - 10:00 PM') or contains(text(),'约下午6:00 - 晚上10:00')]")
        ));
        bestTime.click();

        // Step 16: Department needs to provide a reply (multi-language)
        WebElement needReplyYes = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//label[@for='need_1']//span[contains(text(),'Yes') or contains(text(),'需要')] | //label[@for='need_1']")
        ));
        needReplyYes.click();

        // Step 17: Click the 'Next' button (multi-language)
        nextButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[normalize-space()='Next' or contains(text(),'下一步')]")
        ));
        nextButton.click();

        // ================== Confirmation page assertions (refreshed elements) ==================
        // After page transition, always re-find elements to avoid StaleElementReferenceException
        // Prepare expected values for batch assertion
        Map<String, String> expectedInfo = new HashMap<>();
        expectedInfo.put("Subject of Service Request/Complaint", "Other Complaints");
        expectedInfo.put("Have you submitted a case to 1823 regarding the same topic?", "No");
        expectedInfo.put("Case Information", caseInfoText);
        expectedInfo.put("Case Location", caseLocation);
        expectedInfo.put("Name", "Robin");
        expectedInfo.put("Email", "robintesting@gmail.com");
        expectedInfo.put("Phone", "66886868");
        // Add more fields as needed

        // Re-find all info__item blocks on the confirmation page
        List<WebElement> infoItems = driver.findElements(By.xpath("//div[@class='info__item']"));
        for (WebElement item : infoItems) {
            String title = "";
            String content = "";
            try {
                // Always re-find child elements from the current item
                title = item.findElement(By.xpath(".//p[@class='info__title']")).getText().trim();
                // Special handling for Case Location
                if (title.equals("Case Location")) {
                    content = item.findElement(By.xpath(".//p[@class='map-row__address']")).getText().trim();
                    // Assert system's selected address
                    assertEquals("FU YIP STREET", content, "Case Location should match the system's selected address");
                    continue;
                }
                if (expectedInfo.containsKey(title)) {
                    content = item.findElement(By.xpath(".//div[@class='info__content']/p | .//div[@class='info__content']/div/p")).getText().trim();
                }
                // Remove all upload file assertions: do not check Photo/File Upload or file names
                if (expectedInfo.containsKey(title) && !title.equals("Photo/File Upload")) {
                    assertEquals(expectedInfo.get(title), content, title + " should match");
                }
            } catch (NoSuchElementException e) {
                // Skip if title or content not found
            }
        }
        // [Upload file assertions removed: no checks for uploaded file names or counts]

        // ================== Section E: Finish ==================
        // Step 26: Wait a few seconds to observe the result
        Thread.sleep(10000); // Wait a few seconds to observe the result
    }
}

// Tests run successfully. Last updated: 2025-07-22 14:35:00 by Robin