package hk1823.automation.Utility.terry.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.microsoft.playwright.options.AriaRole;
import java.nio.file.Paths;
import com.microsoft.playwright.FileChooser;

public class GarbageComplaintTest {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    //@BeforeAll
    static void setup() {
        playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setChannel("chrome") // Use Chrome channel
                .setHeadless(false); // Set to false to see the browser
        //        .setSlowMo(1000); // Slow down operations for visibility
        browser = browserType.launch(options);
        context = browser.newContext();
        page = context.newPage();

        page.navigate("https://www.1823.gov.hk/en");
        page.locator("//span[@class='menu__text menu__text--lv1'][normalize-space()='Request for Service/Complain']").click();
        page.locator("//a[@value='64a7256d88403fe3696c000000000028']").click();
    }

    //@AfterAll
    static void tearDown() {
        page.close();
        context.close();
        playwright.close();
    }

    //@Test
    void submitAndVerifyComplaint() throws InterruptedException {
        Locator refusePublicPlaces = page.locator("//span[normalize-space()='Refuse in public places']");
        refusePublicPlaces.click();
        Locator nextButton = page.getByRole(AriaRole.BUTTON,
                                            new Page.GetByRoleOptions().setName("Next"));
        nextButton.click();
        
        Locator domesticWaste = page.locator("label[for='ed4bb07ed9584496bb5a514f7fc8aefb_0'] span");
        domesticWaste.click();
        nextButton.click();

        Locator noRadioButton_1 = page.locator("(//span[normalize-space()='No'])[1]");
        noRadioButton_1.click();
        Locator supplementInfo = page.locator("textarea[id='Supplementary Information']");
        supplementInfo.fill("Supplementary information about garbage...");

        Locator addressInput = page.getByPlaceholder("Enter location");
        addressInput.fill("26 Nathan Road");
        Thread.sleep(3000); // Wait for suggestions to load
        Locator addressSuggest = page.getByText("26 Nathan Road");
        addressSuggest.click();

        FileChooser fileChooser = page.waitForFileChooser(() -> page.getByText("Select or drag and drop files to here").click());
        fileChooser.setFiles(Paths.get("C:\\Users\\TERRY\\OneDrive\\Desktop\\testing.jpg"));
        Thread.sleep(3000); // Wait for file upload to complete
        nextButton.click();

        Locator agree_1 = page.locator("(//span[contains(text(),'Yes')])[1]");
        agree_1.click();
        Locator agree_2 = page.locator("(//span[contains(text(),'Yes')])[2]");
        agree_2.click();
        Locator nameInput = page.locator("#name");
        nameInput.fill("Tester Name");
        Locator emailInput = page.locator("#email");
        emailInput.fill("test@example.com");
        Locator phoneInput = page.locator("#phone");
        phoneInput.fill("12345678");
        Locator timeSlot = page.locator("label[for='time_1'] span");
        timeSlot.click();
        Locator noRadioButton_3 = page.locator("(//span[contains(text(),'No')])[3]");
        noRadioButton_3.click();
        nextButton.click();

        // Thread.sleep(8000);
        // Locator submitButton = page.getByRole(AriaRole.BUTTON,
        //                                     new Page.GetByRoleOptions().setName("Submit"));
        // submitButton.click();
        // Thread.sleep(5000);
    }

    //@Test
    void verifyComplaintDetails() throws InterruptedException {
        String[] expectedResults = {
                "Clean-up of Refuses or Streets", //0
                "Refuse in public places", //1
                "Domestic waste", //2
                "No", //3
                "-", //4
                "Supplementary information about garbage...", //5
                "26 Nathan Road", //6
                "testing.jpg", //7
                "Tester Name", //8
                "test@example.com", //9
                "12345678", //10
                "approximately 8:00 AM - 12:00 noon", //11
                "I agree to disclose the personal data (including name, email address and telephone number) to the 1823, and the relevant Government departments, related organisations and Contractors for handling this case.", //12
                "No" //13
        };

        for (int i = 1; i <= 5; i++) {
        System.out.println(page.locator("(//div[@class='info__content'])["+(i)+"]")
                         .textContent().trim());
            // assertEquals(expectedResults[i],
            //             page.locator("(//div[@class='info__content'])["+(i)+"]")
            //             .textContent().trim());
        }

    //     assertEquals(expectedResults[6], 
    //            page.locator("//p[@class='map-row__address']").textContent().trim());

    //     for (int i = 7; i <= 12; i++) {
    //        assertEquals(expectedResults[i], 
    //                page.locator("(//div[@class='info__content'])[" + (i + 1) + "]").textContent().trim());
    //     }

    //     assertEquals(expectedResults[13], 
    //            page.locator("(//div[@class='info__content'])[19]").textContent().trim());
    // }
    }
}