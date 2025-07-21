package academy.teenfuture.crse.utility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.Page;

import com.microsoft.playwright.Locator;

import org.junit.jupiter.api.AfterAll;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Objects;

public class HandleSliderTests {

    @Test

    public void HandleSlider_Sample1() throws InterruptedException {
        Playwright playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();

        page.navigate("https://cleartax.in/s/sip-calculator");

        Locator img = page.locator("//div[contains(@class,'flex items-start')]");
        img.scrollIntoViewIfNeeded();

        ElementHandle slider = page.querySelector("(//input[@id='slider'])[2]");

        int expectValue = 27;
        boolean isCompleted = false;

        while (!isCompleted) {
            int actualValue = Integer.parseInt(slider.getAttribute("value"));
            System.out.println("slider : " + actualValue);
            if (actualValue == expectValue) {
                isCompleted = true;
                break;
            }
            slider.press(expectValue > actualValue ? "ArrowRight" : "ArrowLeft");
        }
        page.waitForTimeout(3000);
        playwright.close();
    }

    @Test
    public void HandleSlider_Sample2() {
        Playwright playwright = Playwright.create();
        BrowserType browserType = Playwright.create().chromium();
        Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();

        page.navigate("https://www.hdfcbank.com/personal/tools-and-calculators/sip-calculator");

        Locator slider = page.locator("#js-rangeslider-0");
        Locator sliderBtn = slider.locator(".rangeslider__handle");

        String expectText = "50";

        boolean isCompleted = false;
        while (!isCompleted) {
            String sliderText = page.locator("//div[@class='form-group mB5']//output[1]").innerText();
            System.out.println("sliderText : " + sliderText);

            if (Objects.equals(sliderText, expectText)) {
                isCompleted = true;
                break;
            }
            BoundingBox sliderInputBox = sliderBtn.boundingBox();
            // from
            double startX = sliderInputBox.x + sliderInputBox.width / 2;
            double startY = sliderInputBox.y;
            // to
            double endX = sliderInputBox.x + 15;
            double endY = sliderInputBox.y;

            if (sliderInputBox != null) {
                page.mouse().move(startX, startY);
                page.mouse().down();
                page.mouse().move(endX, endY);
                page.mouse().up();
            }
        }

        page.waitForTimeout(5000);
        playwright.close();
    }

}