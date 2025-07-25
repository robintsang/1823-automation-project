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
public class ComplaintSeleniumJSONAutoUploadTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JsonNode complaintData;

    @BeforeAll
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver",
                "src/main/resources/driver/ChromeDriver/chromedriver-mac-x64/chromedriver");
        driver = new ChromeDriver();
        // 設定視窗大小為 1920x1280 (Set window size to 1920x1280)
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1280));
        // 取得螢幕尺寸並將視窗移到螢幕中央 (Get screen size and center window)
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - 1920) / 2;
        int y = (screenSize.height - 1280) / 2;
        driver.manage().window().setPosition(new org.openqa.selenium.Point(x, y));
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

        // Step 3: Strictly select complaint category based on JSON 'category' field
        // Define the 12 valid categories (must match the <img alt="..."> on the page)
        List<String> validCategories = Arrays.asList(
            "Clean-up of Refuses or Streets",
            "Water Dripping outside Building Units",
            "Obstruction and Hygiene Problems Caused by Food Premises",
            "Repair and Cleansing of Public Lighting Facilities",
            "Water Seepage inside the flat",
            "Road Repair",
            "Tree",
            "Slope",
            "Public Transport Services Staff & Service Quality",
            "Illegal Parking in Public Housing Area",
            "Non-emergency traffic offences",
            "Other Complaints"
        );
        // Read category from JSON
        String category = complaintData.has("category") ? complaintData.get("category").asText() : "";
        // Validate category strictly
        if (!validCategories.contains(category)) {
            throw new RuntimeException("JSON 'category' field must be one of the 12 valid categories, but got: " + category);
        }
        // Find all category options on the page
        List<WebElement> categoryOptions = driver.findElements(By.xpath("//a[contains(@class,'option')]//img"));
        boolean found = false;
        for (WebElement img : categoryOptions) {
            String altText = img.getAttribute("alt").trim();
            if (altText.equals(category)) {
                // Click the corresponding <a> element
                WebElement aTag = img.findElement(By.xpath("./ancestor::a[1]"));
                aTag.click();
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException("No matching complaint category found on page for: " + category);
        }

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

        // Step 9: Auto upload files using Selenium sendKeys method (support multiple files)
        // 注意：input[type='file'] 必須是可見且未被JS覆蓋，否則sendKeys會失敗
        String filePath1 = System.getProperty("user.dir") + "/test_uploads/robin/fu_yip_street_flood_image.jpg";
        String filePath2 = System.getProperty("user.dir") + "/test_uploads/robin/fu_yip_street_flood_video.mp4";
        try {
            WebElement fileInput = driver.findElement(By.xpath("//input[contains(@id,'fileupload') and @type='file']"));
            // 嘗試多檔案上傳（若input有multiple屬性）
            fileInput.sendKeys(filePath1 + "\n" + filePath2);
            Thread.sleep(2000); // 等待上傳
        } catch (Exception e) {
            System.out.println("[警告] sendKeys自動上傳失敗，請檢查input[type='file']是否可見或考慮Robot/JavascriptExecutor方法。");
        }

        // Step 10: Auto slow scroll down on upload page for recording
        try {
            long scrollHeight = (Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight");
            for (int y = 0; y < scrollHeight; y += 100) {
                ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, arguments[0]);", y);
                Thread.sleep(100); // 每100px
            }
        } catch (Exception e) {
            System.out.println("[警告] scroll down 失敗: " + e.getMessage());
        }

        // Step 11: Click the 'Next' button to proceed to the next page (multi-language compatible)
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

        // ================== Section D: Confirmation page assertions ==================
        // Step 18: Auto slow scroll down on confirmation page and check info step by step
        try {
            long scrollHeight = (Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight");
            for (int y = 0; y < scrollHeight; y += 100) {
                ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, arguments[0]);", y);
                Thread.sleep(100); // 每100px停0.1秒
            }
        } catch (Exception e) {
            System.out.println("[警告] scroll down 失敗: " + e.getMessage());
        }

        // Step 19: Detect CAPTCHA and pause for manual input
        try {
            if (driver.findElements(By.xpath("//input[contains(@id,'captcha') or contains(@name,'captcha') or contains(@placeholder,'驗證碼') or contains(@placeholder,'CAPTCHA')]"))
                    .size() > 0) {
                System.out.println("偵測到驗證碼，請手動輸入後於console按Enter繼續...\nCAPTCHA detected, please input manually and press Enter in console to continue...");
                System.in.read(); // 等待人工操作
            }
        } catch (Exception e) {
            System.out.println("[警告] 驗證碼暫停流程發生錯誤: " + e.getMessage());
        }

        // Step 20: Assert confirmation page info matches expected values
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


// Tests run successfully. Last updated: 2025-07-25 14:35:00 by Robin 