package academy.teenfuture.crse.tility;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.*;

public class PlayWrightFeedbackFormTests {
    @Test
    public void TestFeedbackForm() throws InterruptedException {

        // Start Playwright =================
        Playwright playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        Page page = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false)).newContext(
            new Browser.NewContextOptions()
                .setHttpCredentials("student", "teenfuture")
        ).newPage();
        page.navigate("https://pt-rv.teenfuture.academy/tfa/autotest/playwright/form-exec1.html");

        String FeedbackInputXPath = "//textarea[@id='input_4']";
        String FirstNameInputXPath = "//input[@id='first_8']";
        String LastNameInputXPath = "//input[@id='last_8']";
        String EmailInputXPath = "//input[@id='input_6']";
        String SubmitButtonXPath = "//button[@id='input_9']";

        Locator FeedbackTextareaInput = page.locator(FeedbackInputXPath);
        Locator FirstNameInput = page.locator(FirstNameInputXPath);
        Locator LastNameInput = page.locator(LastNameInputXPath);
        Locator EmailInput = page.locator(EmailInputXPath);
        Locator SubmitButton = page.locator(SubmitButtonXPath);

        // Fill in the form
        FeedbackTextareaInput.fill("This is a feedback form test.");
        Thread.sleep(2000); 

        FirstNameInput.fill("John");
        Thread.sleep(2000); 

        LastNameInput.fill("Doe");
        Thread.sleep(2000); 

        EmailInput.fill("abc@123.com");
        Thread.sleep(2000); 

        SubmitButton.click();
        Thread.sleep(2000); 

        // Stop for a moment before end =================
        Thread.sleep(5000);

        // Close Playwright =================
        playwright.close();
    }

    
    
}