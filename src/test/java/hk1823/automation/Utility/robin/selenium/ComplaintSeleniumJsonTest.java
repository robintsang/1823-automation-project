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
import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ComplaintSeleniumJsonTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JsonNode complaintData;

    @BeforeAll
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver",
                "src/main/resources/driver/ChromeDriver/chromedriver-mac-x64/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Load JSON data
        ObjectMapper mapper = new ObjectMapper();
        complaintData = mapper.readTree(Files.readAllBytes(Paths.get("complaints/complaint_robin.json")));
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
        String caseInfoText = complaintData.has("description") ? complaintData.get("description").asText() : "";

        // Step 6: Fill in the case information
        WebElement caseInfoTextarea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@id='個案資料' or @id='个案资料' or @id='Case Information']")));
        caseInfoTextarea.sendKeys(caseInfoText);

        // Step 7: Fill in the case location (multi-language compatible)
        String caseLocation = complaintData.has("location") ? complaintData.get("location").asText() : "FU YIP STREET and HUNG LOK STREET junction";
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
        // 請於手動上傳時，從 test_uploads/terry/ 目錄選擇 Terry 的測試圖片
        // When uploading manually, please select Terry's test images from test_uploads/terry/
        System.out.println("[INFO] Please upload the file manually at this point in the demo video.");
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
        driver.findElement(By.xpath("//input[@id='name']")).sendKeys(complaintData.has("name") ? complaintData.get("name").asText() : "Robin");
        driver.findElement(By.xpath("//input[@id='email']")).sendKeys(complaintData.has("email") ? complaintData.get("email").asText() : "robintesting@gmail.com");
        driver.findElement(By.xpath("//input[@id='phone']")).sendKeys(complaintData.has("phone") ? complaintData.get("phone").asText() : "66886868");

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
        Map<String, String> expectedInfo = new HashMap<>();
        expectedInfo.put("Subject of Service Request/Complaint", "Other Complaints");
        expectedInfo.put("Have you submitted a case to 1823 regarding the same topic?", "No");
        expectedInfo.put("Case Information", caseInfoText);
        expectedInfo.put("Case Location", caseLocation);
        expectedInfo.put("Name", complaintData.has("name") ? complaintData.get("name").asText() : "Robin");
        expectedInfo.put("Email", complaintData.has("email") ? complaintData.get("email").asText() : "robintesting@gmail.com");
        expectedInfo.put("Phone", complaintData.has("phone") ? complaintData.get("phone").asText() : "66886868");
        // Add more fields as needed

        List<WebElement> infoItems = driver.findElements(By.xpath("//div[@class='info__item']"));
        for (WebElement item : infoItems) {
            String title = "";
            String content = "";
            try {
                title = item.findElement(By.xpath(".//p[@class='info__title']")).getText().trim();
                if (title.equals("Case Location")) {
                    content = item.findElement(By.xpath(".//p[@class='map-row__address']")).getText().trim();
                    assertEquals("FU YIP STREET", content, "Case Location should match the system's selected address");
                    continue;
                }
                if (expectedInfo.containsKey(title)) {
                    content = item.findElement(By.xpath(".//div[@class='info__content']/p | .//div[@class='info__content']/div/p")).getText().trim();
                }
                if (expectedInfo.containsKey(title) && !title.equals("Photo/File Upload")) {
                    assertEquals(expectedInfo.get(title), content, title + " should match");
                }
            } catch (NoSuchElementException e) {
            }
        }
        Thread.sleep(10000);
    }
} 


// Tests run successfully. Last updated: 2025-07-22 14:35:00 by Robin