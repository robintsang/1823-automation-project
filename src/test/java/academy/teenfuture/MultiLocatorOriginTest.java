package academy.teenfuture.crse.utility;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.*;

public class MultiLocatorOriginTest {
    @Test
    public void multiLocator() throws InterruptedException {

        // Start Playwright =================
        Playwright playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        Page page = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false)).newContext().newPage();
        page.navigate("https://www.hko.gov.hk/");

        // all() method in Locator class can get all the matched elements
        List<Locator> navBarItems = page.locator(".subheaderMenu > ul > li > a > .myText").all();
        for (Locator item : navBarItems) {
            // Print all results
            System.out.println(item.innerText());
            // System.out.println(item.innerHTML());
        }

        // Stop for a moment before end =================
        Thread.sleep(5000);

        // Close Playwright =================
        playwright.close();
    }

    @Test
    public void multiLocatorPwAssertion() throws InterruptedException {
        String[] expectedResults = {
                "天氣",
                "氣候",
                "地球物理",
                "天文及授時",
                "輻射監測",
                "社群",
                "學習資源", // Make a wrong answer
                "國際及區域合作",
                "媒體及消息",
                "電子服務",
                "關於我們"
        };

        // Start Playwright =================
        Playwright playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        Page page = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false)).newContext().newPage();
        page.navigate("https://www.hko.gov.hk/tc");

        // Method 1:
        // Use all() method in Locator class can get all the matched elements, then for loop checking
        //
        List<Locator> navBarItems = page.locator(".subheaderMenu > ul > li > a > .myText").all();
        for (int i = 0; i < expectedResults.length; i++) {
            // Print all results
            System.out.println(navBarItems.get(i).innerText());
            PlaywrightAssertions.assertThat(navBarItems.get(i)).hasText(expectedResults[i]);
        }

        // Method 2:
        // One line code compares multiple locators with array
        // PlaywrightAssertions.assertThat(page.locator(".subheaderMenu > ul > li > a > .myText")).hasText(expectedResults);

        // Stop for a moment before end =================
        Thread.sleep(5000);

        // Close Playwright =================
        playwright.close();
    }
}