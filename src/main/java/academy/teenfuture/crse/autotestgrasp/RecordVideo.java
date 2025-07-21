package academy.teenfuture.crse.autotestgrasp;

import academy.teenfuture.crse.autotestgrasp.UserAgentData;
import com.microsoft.playwright.*;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Playwright;

import java.nio.file.Paths;

public class RecordVideo {

    private final Playwright playwright;

    public RecordVideo(Playwright playwright) {
        this.playwright = playwright;
    }

    public BrowserContext createBrowserContext() {
        UserAgentData userAgentData = new UserAgentData();
        String userAgent = userAgentData.getRandomUserAgent();

        try {
            Browser browser = playwright.chromium().launch();
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent(userAgent)
                    .setRecordVideoDir(Paths.get("videos/"))
                    .setRecordVideoSize(1920, 1080));
            return context;
        } catch (PlaywrightException e) {
            System.out.println("Error creating browser context: " + e.getMessage());
            return null;
        }
    }

}