package hk1823.automation.Utility.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplaintPlaywrightJsonTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    static JsonNode complaintData;

    @BeforeAll
    static void setupAll() throws Exception {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        // Load JSON data
        ObjectMapper mapper = new ObjectMapper();
        complaintData = mapper.readTree(Files.readAllBytes(Paths.get("complaints/complaint_robin.json")));
    }

    @AfterAll
    static void teardownAll() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void teardown() {
        if (context != null) context.close();
    }

    @Test
    public void testOtherCategoryComplaintJson() throws InterruptedException {
        // ================== Section A: Open website and enter complaint form ==================
        // Step 1: Open the homepage
        page.navigate("https://www.1823.gov.hk/en/");

        // Step 2: Click the 'Request for Service/Complaint' button in the top navigation
        page.click("xpath=//a[contains(@href, '/form/complain') and contains(@class, 'menu__link--lv1')]");

        // Step 3: Wait for the category selection page and click 'Others'
        page.waitForSelector("xpath=//a[@value='64a7256d88403fe3696c000000000151']");
        page.click("xpath=//a[@value='64a7256d88403fe3696c000000000151']");

        // ================== Section B: Fill in complaint content ==================
        // Step 4: Wait for the content form to appear and select 'No' (multi-language compatible)
        page.waitForSelector("xpath=//span[contains(text(),'沒有') or normalize-space()='No' or contains(text(),'没有')]/ancestor::label");
        page.click("xpath=//span[contains(text(),'沒有') or normalize-space()='No' or contains(text(),'没有')]/ancestor::label");

        // Step 5: Detect page language by the main title and set the case information text accordingly
        String titleText = page.textContent("xpath=//span[@class='form-name__lg']").trim();
        String caseInfoText = complaintData.has("description") ? complaintData.get("description").asText() : "";

        // Step 6: Fill in the case information
        page.fill("xpath=//textarea[@id='個案資料' or @id='个案资料' or @id='Case Information']", caseInfoText);

        // Step 7: Fill in the case location
        String caseLocation = complaintData.has("location") ? complaintData.get("location").asText() : "FU YIP STREET and HUNG LOK STREET junction";
        page.fill("xpath=//input[@id='suggest' or @placeholder='輸入地點' or @placeholder='Enter location' or @placeholder='输入地点']", caseLocation);
        page.waitForSelector("xpath=//ul[contains(@class,'ui-autocomplete') and not(contains(@style,'display: none'))]//div[1]");
        page.click("xpath=//ul[contains(@class,'ui-autocomplete') and not(contains(@style,'display: none'))]//div[1]");

        // ================== Section C: Auto upload files (Playwright only) ==================
        // Step 8: Attempt to automatically upload multiple files (image + video)
        String filePath = System.getProperty("user.dir") + "/test_uploads/fu_yip_street_flood_image.jpg";
        String videoPath = System.getProperty("user.dir") + "/test_uploads/fu_yip_street_flood_video.mp4";
        page.setInputFiles("xpath=//input[contains(@id,'fileupload') and @type='file']",
            new java.nio.file.Path[] { Paths.get(filePath), Paths.get(videoPath) });
        page.waitForTimeout(2000);

        // Step 9: Click the 'Next' button to proceed to the next page
        page.click("xpath=//button[normalize-space()='下一步' or normalize-space()='Next']");

        // ================== Section D: Fill in personal information ==================
        // Step 10: Agree to provide contact information (multi-language)
        page.click("xpath=//label[@for='agree_1823_1']//span[contains(text(),'Yes') or contains(text(),'同意')]");
        // Step 11: Agree to disclose personal data (multi-language)
        page.click("xpath=//label[@for='agree_1']//span[contains(text(),'同意') or contains(text(),'Yes')]");
        // Step 12: Fill in Name, Email, Phone
        page.fill("xpath=//input[@id='name']", complaintData.has("name") ? complaintData.get("name").asText() : "Robin");
        page.fill("xpath=//input[@id='email']", complaintData.has("email") ? complaintData.get("email").asText() : "robintesting@gmail.com");
        page.fill("xpath=//input[@id='phone']", complaintData.has("phone") ? complaintData.get("phone").asText() : "66886868");
        // Step 13: Select best time to call (multi-language)
        page.click("xpath=//span[contains(text(),'約下午6:00 - 晚上10:00') or contains(text(),'approximately 6:00 PM - 10:00 PM') or contains(text(),'约下午6:00 - 晚上10:00')]");
        // Step 14: Department needs to provide a reply (multi-language)
        page.click("xpath=//label[@for='need_1']//span[contains(text(),'Yes') or contains(text(),'需要')] | //label[@for='need_1']");

        // Step 15: Click the 'Next' button
        page.click("xpath=//button[normalize-space()='Next' or contains(text(),'下一步')]");

        // ================== Section E: Confirmation page assertions (no upload file checks) ==================
        Map<String, String> expectedInfo = new HashMap<>();
        expectedInfo.put("Subject of Service Request/Complaint", "Other Complaints");
        expectedInfo.put("Have you submitted a case to 1823 regarding the same topic?", "No");
        expectedInfo.put("Case Information", caseInfoText);
        expectedInfo.put("Case Location", caseLocation);
        expectedInfo.put("Name", complaintData.has("name") ? complaintData.get("name").asText() : "Robin");
        expectedInfo.put("Email", complaintData.has("email") ? complaintData.get("email").asText() : "robintesting@gmail.com");
        expectedInfo.put("Phone", complaintData.has("phone") ? complaintData.get("phone").asText() : "66886868");
        // Add more fields as needed

        int infoItemCount = page.locator("xpath=//div[@class='info__item']").count();
        for (int i = 0; i < infoItemCount; i++) {
            Locator item = page.locator("xpath=//div[@class='info__item']").nth(i);
            String title = "";
            String content = "";
            try {
                title = item.locator("xpath=.//p[@class='info__title']").textContent().trim();
                if (title.equals("Case Location")) {
                    content = item.locator("xpath=.//p[@class='map-row__address']").textContent().trim();
                    assertEquals("FU YIP STREET", content, "Case Location should match the system's selected address");
                    continue;
                }
                if (expectedInfo.containsKey(title)) {
                    Locator contentLocator = item.locator("xpath=.//div[@class='info__content']/p | .//div[@class='info__content']/div/p");
                    if (contentLocator.count() > 0) {
                        content = contentLocator.first().textContent().trim();
                    }
                }
                if (expectedInfo.containsKey(title) && !title.equals("Photo/File Upload")) {
                    assertEquals(expectedInfo.get(title), content, title + " should match");
                }
            } catch (Exception e) {
            }
        }
        page.waitForTimeout(10000);
    }
} 