package hk1823.automation.Utility.robin.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Arrays;

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
        // 設定瀏覽器視窗大小為 1920x1280 (Set browser viewport to 1920x1280)
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1280));
        page = context.newPage();
        // 自動將 Chromium 視窗移到螢幕中央 (Center Chromium window on screen)
        try {
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - 1920) / 2;
            int y = (screenSize.height - 1280) / 2;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("mac")) {
                // macOS: AppleScript
                String script = String.format(
                    "osascript -e 'tell application \"Google Chrome\" to set the bounds of the first window to {%d, %d, %d, %d}'",
                    x, y, x + 1920, y + 1280
                );
                Runtime.getRuntime().exec(script);
            } else if (os.contains("win")) {
                // Windows: PowerShell
                String ps = String.format(
                    "$hwnd = (Get-Process chrome | Where-Object {{$_.MainWindowTitle}} | Select-Object -First 1).MainWindowHandle; " +
                    "Add-Type -TypeDefinition '[DllImport(\"user32.dll\")]public static extern bool MoveWindow(IntPtr hWnd, int X, int Y, int nWidth, int nHeight, bool bRepaint);' -Name Win32 -Namespace Native; " +
                    "[Native.Win32]::MoveWindow($hwnd, %d, %d, %d, %d, $true);",
                    x, y, 1920, 1280
                );
                String[] cmd = {"powershell", "-Command", ps};
                Runtime.getRuntime().exec(cmd);
            }
        } catch (Exception e) {
            System.out.println("[警告] 無法自動置中 Chromium 視窗: " + e.getMessage());
        }
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

        // Step 3: Strictly select complaint category based on JSON 'category' field
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
        if (!validCategories.contains(category)) {
            throw new RuntimeException("JSON 'category' field must be one of the 12 valid categories, but got: " + category);
        }
        // Wait for all category options to be present
        page.waitForSelector("a.option img");
        boolean found = false;
        for (ElementHandle img : page.querySelectorAll("a.option img")) {
            String altText = img.getAttribute("alt").trim();
            if (altText.equals(category)) {
                img.evaluate("node => node.closest('a').click()");
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException("No matching complaint category found on page for: " + category);
        }

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
        // 將測試圖片和影片路徑改為 Robin 專屬目錄
        // Change test image and video path to Robin's directory
        String filePath = System.getProperty("user.dir") + "/test_uploads/robin/fu_yip_street_flood_image.jpg";
        String videoPath = System.getProperty("user.dir") + "/test_uploads/robin/fu_yip_street_flood_video.mp4";
        page.setInputFiles("xpath=//input[contains(@id,'fileupload') and @type='file']",
            new java.nio.file.Path[] { Paths.get(filePath), Paths.get(videoPath) });
        page.waitForTimeout(2000);
        // 只将上传区滚动到可见，不再全页 scroll down
        // Scroll upload area into view only (不要全页 scroll)
        page.locator("xpath=//input[contains(@id,'fileupload') and @type='file']").scrollIntoViewIfNeeded();

        // Step 9: Click the 'Next' button to proceed to the next page
        page.click("xpath=//button[normalize-space()='下一步' or normalize-space()='Next']");

        // Step 10: Auto slow scroll down for recording
        page.waitForTimeout(500); // 等待頁面穩定
        int scrollHeight = (int) page.evaluate("() => document.body.scrollHeight");
        for (int y = 0; y < scrollHeight; y += 100) {
            page.evaluate("window.scrollTo(0, " + y + ")");
            page.waitForTimeout(100);
        }

        // ================== Section D: Fill in personal information ==================
        // Step 11: Agree to provide contact information (multi-language)
        page.click("xpath=//label[@for='agree_1823_1']//span[contains(text(),'Yes') or contains(text(),'同意')]");
        // Step 12: Agree to disclose personal data (multi-language)
        page.click("xpath=//label[@for='agree_1']//span[contains(text(),'同意') or contains(text(),'Yes')]");
        // Step 13: Fill in Name, Email, Phone
        page.fill("xpath=//input[@id='name']", complaintData.has("name") ? complaintData.get("name").asText() : "Robin");
        page.fill("xpath=//input[@id='email']", complaintData.has("email") ? complaintData.get("email").asText() : "robintesting@gmail.com");
        page.fill("xpath=//input[@id='phone']", complaintData.has("phone") ? complaintData.get("phone").asText() : "66886868");
        // Step 14: Select best time to call (multi-language)
        page.click("xpath=//span[contains(text(),'約下午6:00 - 晚上10:00') or contains(text(),'approximately 6:00 PM - 10:00 PM') or contains(text(),'约下午6:00 - 晚上10:00')]");
        // Step 15: Department needs to provide a reply (multi-language)
        page.click("xpath=//label[@for='need_1']//span[contains(text(),'Yes') or contains(text(),'需要')] | //label[@for='need_1']");

        // Step 16: Click the 'Next' button
        page.click("xpath=//button[normalize-space()='Next' or contains(text(),'下一步')]");

        // ================== Section E: Confirmation page assertions (no upload file checks) ==================
        // Step 17: Auto slow scroll down for recording
        page.waitForTimeout(500);
        scrollHeight = (int) page.evaluate("() => document.body.scrollHeight");
        for (int y = 0; y < scrollHeight; y += 100) {
            page.evaluate("window.scrollTo(0, " + y + ")");
            page.waitForTimeout(100);
        }

        // Step 18: Assert confirmation page info matches expected values
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