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

import java.nio.file.FileSystems;
import java.nio.file.Path;


/*
 * Your test for PlayWright
 */
public class Play3WrightTests {

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
    public void browserTest3() throws InterruptedException {
        String userDataDir = "./user-data/firefox";
        Path path = FileSystems.getDefault().getPath(userDataDir);

        // Start Playwright =================
        Playwright playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        // 
        BrowserContext bc = browserType.launchPersistentContext(path, new BrowserType.LaunchPersistentContextOptions().setHeadless(false));
        Page page = bc.newPage();
        //
        page.navigate("https://www.google.com.hk/");

        // Stop for a moment before end =================
        Thread.sleep(19000);
        
        // Close Playwright =================
        playwright.close();
    }


}
