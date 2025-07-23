package hk1823.automation.Utility.suki.playwright;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;

public class My1823BrowserTest {
    @Test
    public void MultiBrowserTests1() throws InterruptedException {
      
      // Start Playwright =================
      Playwright playwright = Playwright.create();
      String targetUrl = "https://www.1823.gov.hk/tc";
      //Chromium
      Browser chromium = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
      Page chromiumPage = chromium.newContext().newPage();
      chromiumPage.navigate(targetUrl);
      Thread.sleep(5000);
      chromium.close();
     // Firefox
     Browser firefox = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
      Page firefoxPage = firefox.newContext().newPage();
      firefoxPage.navigate(targetUrl);
      Thread.sleep(5000);
      firefox.close();
      // Edge
      Browser edge = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));
      Page edgePage = edge.newContext().newPage();
      edgePage.navigate(targetUrl);
      Thread.sleep(5000);
      edge.close();
      
      playwright.close();
      
    }
  }