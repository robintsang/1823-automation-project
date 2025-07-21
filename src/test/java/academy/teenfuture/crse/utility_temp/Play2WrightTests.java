package academy.teenfuture.crse.utility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Page;


import com.microsoft.playwright.Locator;


import org.junit.jupiter.api.AfterAll;


/*
 * Your test for PlayWright
 */
public class Play2WrightTests {

  @BeforeAll
  public static void setup() {
  }

  @Test
  public void testCase1() {

    MakeVideosPlayWright makeVideosPlayWright = new MakeVideosPlayWright();
    makeVideosPlayWright.start();
    
    // your code for testCase1
    System.out.println("testCase1");
    
    makeVideosPlayWright.stop();    
  }

  @Test
  public void testCase2() {
  
    MakeVideosPlayWright makeVideosPlayWright = new MakeVideosPlayWright();
    makeVideosPlayWright.start();
  
    // your code for testCase2
    System.out.println("testCase2");
  
    makeVideosPlayWright.stop();    
  }
  
  @AfterAll
  public static void bye() {
  }

  // @Test
  // public void sample1() throws InterruptedException {

  //   // Start Playwright =================
  //   Playwright playwright = Playwright.create();
  //   BrowserType browserType = playwright.chromium(); //webkit();
  //   Page page = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false)).newContext().newPage();
  //   page.navigate("https://www.hko.gov.hk/");
    
  //   // Stop for a moment before end =================
  //   Thread.sleep(5000);
    
  //   // Close Playwright =================
  //   playwright.close();
  // }

  // @Test
  // public void sample2() throws InterruptedException {
  //   Playwright playwright = Playwright.create();
  //   BrowserType browserType = playwright.chrome();
  //   Page page = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false)).newContext(
  //       new Browser.NewContextOptions()
  //           .setHttpCredentials("student", "teenfuture")
  //   ).newPage();
  //   page.navigate("https://pt-rv.teenfuture.academy/tfa/autotest/playwright/#/2");
    
  //   Thread.sleep(8000);
  //   playwright.close();
  // }

  @Test
  public void sample2() throws InterruptedException {
    Playwright playwright = Playwright.create();
    BrowserType browserType = playwright.chromium();
    Page page = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false)).newContext(
        new Browser.NewContextOptions()
            .setHttpCredentials("student", "teenfuture")
    ).newPage();
    page.navigate("https://pt-rv.teenfuture.academy/tfa/autotest/playwright/#/3");

    Locator pw = page.locator("form#form2> input#password");
    pw.fill("myPw");

    Locator user1 = page.locator("form#form2> input.userfield");
    user1.fill("myName");
    Thread.sleep(2000);

    Locator user2 = page.locator("form#form2> input#username");
    user2.fill("This is my Name");
    Thread.sleep(2000);

    Locator user3 = page.locator("#form2> #username");
    user3.fill("Java is my Name");
    Thread.sleep(2000);

    Locator user4 = page.locator("#form2> .userfield");
    user4.fill("Super Auto. Testing");
    Thread.sleep(2000);
    
    //=====================================
    // Try your code in this section 

    //=====================================

    page.locator("form#form2 > :text('Login')").click();

    Thread.sleep(8000);
    playwright.close();
  }



}
