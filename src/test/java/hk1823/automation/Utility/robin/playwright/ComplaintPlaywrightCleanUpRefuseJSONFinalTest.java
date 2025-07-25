package hk1823.automation.Utility.robin.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ComplaintPlaywrightCleanUpRefuseJSONFinalTest {
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
                String script = String.format(
                    "osascript -e 'tell application \"Google Chrome\" to set the bounds of the first window to {%d, %d, %d, %d}'",
                    x, y, x + 1920, y + 1280
                );
                Runtime.getRuntime().exec(script);
            } else if (os.contains("win")) {
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

    // 自動檢測並點擊“下一步”按鈕 (Auto-detect and click the 'Next' button if present)
    void clickNextIfExists(Page page) {
        try {
            page.waitForSelector("xpath=//button[normalize-space()='下一步' or normalize-space()='Next']", new Page.WaitForSelectorOptions().setTimeout(2000));
            page.click("xpath=//button[normalize-space()='下一步' or normalize-space()='Next']");
            page.waitForTimeout(500); // 等待頁面切換 (Wait for page transition)
        } catch (Exception e) {
            // 沒有“下一步”按鈕時不報錯 (No error if 'Next' button not found)
        }
    }

    @Test
    public void testCleanUpRefuseCategory() throws InterruptedException, java.io.IOException {
        // Section A: Open website and enter complaint form
        page.navigate("https://www.1823.gov.hk/en/");
        page.click("xpath=//a[contains(@href, '/form/complain') and contains(@class, 'menu__link--lv1')]");

        // Step 3: Select 'Clean-up of Refuses or Streets' category from JSON
        ComplaintData data = new ObjectMapper().readValue(
            Paths.get("complaints/complaint_clean-up_of_refuses_or_streets_2025-07-25T19-43-07-688Z_testing.json").toFile(),
            ComplaintData.class
        );
        String category = data.category;
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

        // Step 4: Dynamically select sub_category_1 and sub_category_1.2 from JSON
        if (data.sub_category_1 != null && !data.sub_category_1.isEmpty()) {
            page.waitForSelector("//span[contains(text(), '" + data.sub_category_1 + "')]");
            page.click("//span[contains(text(), '" + data.sub_category_1 + "')]");
            // Wait for subcategory options to load
            page.waitForTimeout(2000);
            // Screenshot for debug
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("after_main_category.png")));
        }
        if (data.sub_category_1_2 != null && !data.sub_category_1_2.isEmpty()) {
            page.waitForSelector("//span[contains(text(), '" + data.sub_category_1_2 + "')]");
            page.click("//span[contains(text(), '" + data.sub_category_1_2 + "')]");
        }
        clickNextIfExists(page); // Auto click 'Next' after selection

        // Wait for page transition after selecting subcategories
        page.waitForTimeout(1500);

        // Step 5: Detect page language by the main title and set the case information text accordingly
        String titleText = page.textContent("xpath=//span[@class='form-name__lg']").trim();
        String caseInfoText;
        if (titleText.contains("要求服務/投訴")) {
            caseInfoText = "垃圾桶爆滿無人清理影響市容。";
        } else if (titleText.contains("Request for Service/Complaint")) {
            caseInfoText = "Refuse bins are overflowing and have not been emptied, affecting the cityscape.";
        } else if (titleText.contains("要求服务/投诉")) {
            caseInfoText = "垃圾桶爆满无人清理影响市容。";
        } else {
            caseInfoText = "Refuse bins are overflowing and have not been emptied, affecting the cityscape.";
        }

        // Step 6: Fill in the case information (robust for both normal and 'Other complaint' pages)
        page.waitForSelector("xpath=//textarea[@id='Supplementary Information' or @id='Case Information']");
        if (page.locator("xpath=//textarea[@id='Supplementary Information']").count() > 0) {
            page.fill("xpath=//textarea[@id='Supplementary Information']", caseInfoText);
        } else if (page.locator("xpath=//textarea[@id='Case Information']").count() > 0) {
            page.fill("xpath=//textarea[@id='Case Information']", caseInfoText);
        } else {
            throw new RuntimeException("No suitable textarea found for case information!");
        }

        // Step 7: Fill in the case location
        String caseLocation = data.location;
        page.fill("xpath=//input[@id='suggest' or @placeholder='輸入地點' or @placeholder='Enter location' or @placeholder='输入地点']", caseLocation);
        page.waitForSelector("xpath=//ul[contains(@class,'ui-autocomplete') and not(contains(@style,'display: none'))]//div[1]");
        page.click("xpath=//ul[contains(@class,'ui-autocomplete') and not(contains(@style,'display: none'))]//div[1]");

        // Section B: Fill in complaint content
        // Step B-1: Select 'No' for 'Have you submitted a case to 1823 regarding the same topic?'
        page.waitForSelector("xpath=//span[contains(text(),'沒有') or normalize-space()='No' or contains(text(),'没有')]/ancestor::label");
        page.click("xpath=//span[contains(text(),'沒有') or normalize-space()='No' or contains(text(),'没有')]/ancestor::label");

        // Section C: Auto upload files (Playwright only)
        // Step 8: Only upload refuse_example.jpg
        String filePath = System.getProperty("user.dir") + "/test_uploads/robin/refuse_example.jpg";
        page.setInputFiles("xpath=//input[contains(@id,'fileupload') and @type='file']",
            new java.nio.file.Path[] { Paths.get(filePath) });
        page.waitForTimeout(2000);
        page.locator("xpath=//input[contains(@id,'fileupload') and @type='file']").scrollIntoViewIfNeeded();

        // Step 9: Click the 'Next' button to proceed to the next page
        page.click("xpath=//button[normalize-space()='下一步' or normalize-space()='Next']");

        // Step 10: Auto slow scroll down for recording
        page.waitForTimeout(500); // Wait for page to stabilize
        int scrollHeight = (int) page.evaluate("() => document.body.scrollHeight");
        for (int y = 0; y < scrollHeight; y += 100) {
            page.evaluate("window.scrollTo(0, " + y + ")");
            page.waitForTimeout(100);
        }

        // Section D: Fill in personal information
        // Step 11: Agree to provide contact information (multi-language)
        page.click("xpath=//label[@for='agree_1823_1']//span[contains(text(),'Yes') or contains(text(),'同意')]");
        // Step 12: Agree to disclose personal data (multi-language)
        page.click("xpath=//label[@for='agree_1']//span[contains(text(),'同意') or contains(text(),'Yes')]");
        // Step 13: Fill in Name, Email, Phone
        page.fill("xpath=//input[@id='name']", data.name);
        page.fill("xpath=//input[@id='email']", data.email);
        page.fill("xpath=//input[@id='phone']", data.phone);
        // Step 14: Select best time to call (multi-language)
        page.click("xpath=//span[contains(text(),'約下午6:00 - 晚上10:00') or contains(text(),'approximately 6:00 PM - 10:00 PM') or contains(text(),'约下午6:00 - 晚上10:00')]");
        // Step 15: Department needs to provide a reply (multi-language)
        page.click("xpath=//label[@for='need_1']//span[contains(text(),'Yes') or contains(text(),'需要')] | //label[@for='need_1']");

        // Step 16: Click the 'Next' button
        page.click("xpath=//button[normalize-space()='Next' or contains(text(),'下一步')]");

        // Step 17: Auto slow scroll down for recording
        page.waitForTimeout(500); // Wait for page to stabilize
        scrollHeight = (int) page.evaluate("() => document.body.scrollHeight");
        for (int y = 0; y < scrollHeight; y += 100) {
            page.evaluate("window.scrollTo(0, " + y + ")");
            page.waitForTimeout(100);
        }

        // Section E: Confirmation page assertions (no upload file checks)
        // Step 18: Assert confirmation page info matches expected values
        Map<String, String> expectedInfo = new HashMap<>();
        expectedInfo.put("Have you submitted a case to 1823 regarding the same topic?", "No");
        expectedInfo.put("Case Information", caseInfoText);
        expectedInfo.put("Case Location", caseLocation);
        expectedInfo.put("Name", data.name);
        expectedInfo.put("Email", data.email);
        expectedInfo.put("Phone", data.phone);
        // Add more fields as needed

        // Find all info__item blocks on the confirmation page
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
                    // Assert location matches input
                    assertEquals(caseLocation, content, "Case Location should match the system's selected address");
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

        // Section F: Finish
        // Step 26: Wait a few seconds to observe the result
        page.waitForTimeout(10000);
    }
} 