package hk1823.automation.Utility.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplaintPlaywrightTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setupAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
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
    public void testOtherCategoryComplaint() throws InterruptedException {
        // Step 1: Open the homepage
        page.navigate("https://www.1823.gov.hk/en/");

        // Step 2: Click the 'Request for Service/Complaint' button in the top navigation (Selenium XPath)
        page.click("xpath=//a[contains(@href, '/form/complain') and contains(@class, 'menu__link--lv1')]");

        // Step 3: Wait for the category selection page and click 'Others' (Selenium XPath)
        page.waitForSelector("xpath=//a[@value='64a7256d88403fe3696c000000000151']");
        page.click("xpath=//a[@value='64a7256d88403fe3696c000000000151']");

        // Step 4: Wait for the content form to appear and select 'No' (multi-language compatible, Selenium XPath)
        page.waitForSelector("xpath=//span[contains(text(),'沒有') or normalize-space()='No' or contains(text(),'没有')]/ancestor::label");
        page.click("xpath=//span[contains(text(),'沒有') or normalize-space()='No' or contains(text(),'没有')]/ancestor::label");

        // Step 5: Detect page language by the main title and set the case information text accordingly
        String titleText = page.textContent("xpath=//span[@class='form-name__lg']").trim();
        String caseInfoText;
        if (titleText.contains("要求服務/投訴")) {
            caseInfoText = "最近家對面馬路等燈處經常因大雨水浸，渠口堵塞，積水不散。此情況已持續多個月。請盡快安排清理。可提供現場照片及影片。";
        } else if (titleText.contains("Request for Service/Complaint")) {
            caseInfoText = "The road near my home is often flooded after heavy rain. The drains are blocked and the water does not drain. This has been happening for several months. Please arrange for urgent clearance. Photos and videos are available upon request.";
        } else if (titleText.contains("要求服务/投诉")) {
            caseInfoText = "最近家对面马路等灯处经常因大雨水浸，渠口堵塞，积水不散。此情况已持续多个月。请尽快安排清理。可提供现场照片及视频。";
        } else {
            caseInfoText = "The road near my home is often flooded after heavy rain. The drains are blocked and the water does not drain. This has been happening for several months. Please arrange for urgent clearance. Photos and videos are available upon request.";
        }

        // Step 6: Fill in the case information (Selenium XPath)
        page.fill("xpath=//textarea[@id='個案資料' or @id='个案资料' or @id='Case Information']", caseInfoText);

        // Step 7: Fill in the case location (Selenium XPath)
        String caseLocation = "FU YIP STREET and HUNG LOK STREET junction";
        page.fill("xpath=//input[@id='suggest' or @placeholder='輸入地點' or @placeholder='Enter location' or @placeholder='输入地点']", caseLocation);
        page.waitForSelector("xpath=//ul[contains(@class,'ui-autocomplete') and not(contains(@style,'display: none'))]//div[1]");
        page.click("xpath=//ul[contains(@class,'ui-autocomplete') and not(contains(@style,'display: none'))]//div[1]");

        // Step 8: (Manual step in demo video) Please upload the file manually at this point.
        System.out.println("[INFO] Please upload the file manually at this point in the demo video.");
        // Pause to allow manual upload during video recording
        page.waitForTimeout(20000); // 20 seconds for manual upload

        // Step 9: Click the 'Next' button to proceed to the next page (Selenium XPath)
        page.click("xpath=//button[normalize-space()='下一步' or normalize-space()='Next']");

        // Step 10: Fill in personal information (Selenium XPath)
        page.click("xpath=//label[@for='agree_1823_1']//span[contains(text(),'Yes') or contains(text(),'同意')]");
        page.click("xpath=//label[@for='agree_1']//span[contains(text(),'同意') or contains(text(),'Yes')]");
        page.fill("xpath=//input[@id='name']", "Robin");
        page.fill("xpath=//input[@id='email']", "robintesting@gmail.com");
        page.fill("xpath=//input[@id='phone']", "66886868");
        page.click("xpath=//span[contains(text(),'約下午6:00 - 晚上10:00') or contains(text(),'approximately 6:00 PM - 10:00 PM') or contains(text(),'约下午6:00 - 晚上10:00')]");
        page.click("xpath=//label[@for='need_1']//span[contains(text(),'Yes') or contains(text(),'需要')] | //label[@for='need_1']");

        // Step 11: Click the 'Next' button (Selenium XPath)
        page.click("xpath=//button[normalize-space()='Next' or contains(text(),'下一步')]");

        // Step 12: Confirmation page assertions (no upload file checks, Selenium XPath)
        Map<String, String> expectedInfo = new HashMap<>();
        expectedInfo.put("Subject of Service Request/Complaint", "Other Complaints");
        expectedInfo.put("Have you submitted a case to 1823 regarding the same topic?", "No");
        expectedInfo.put("Case Information", caseInfoText);
        expectedInfo.put("Case Location", caseLocation);
        expectedInfo.put("Name", "Robin");
        expectedInfo.put("Email", "robintesting@gmail.com");
        expectedInfo.put("Phone", "66886868");
        // Add more fields as needed

        // Find all info__item blocks on the confirmation page (Selenium XPath)
        int infoItemCount = page.locator("xpath=//div[@class='info__item']").count();
        for (int i = 0; i < infoItemCount; i++) {
            Locator item = page.locator("xpath=//div[@class='info__item']").nth(i);
            String title = "";
            String content = "";
            try {
                title = item.locator("xpath=.//p[@class='info__title']").textContent().trim();
                // Special handling for Case Location
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
                // Do not check Photo/File Upload or file names
                if (expectedInfo.containsKey(title) && !title.equals("Photo/File Upload")) {
                    assertEquals(expectedInfo.get(title), content, title + " should match");
                }
            } catch (Exception e) {
                // Skip if title or content not found
            }
        }

        // Step 13: Wait a few seconds to observe the result
        page.waitForTimeout(10000);
    }
} 