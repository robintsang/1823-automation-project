package academy.teenfuture.crse.Utility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ElementState;

import org.junit.jupiter.api.AfterAll;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.awt.Dimension;
import java.awt.Toolkit;

/*
 * Your test for PlayWright
 */
public class my1823Tests {

  // @Test
  // public void sample1() throws InterruptedException {

  // // Start Playwright =================
  // Playwright playwright = Playwright.create();
  // BrowserType browserType = playwright.chromium(); //webkit();
  // Page page = browserType.launch(new
  // BrowserType.LaunchOptions().setHeadless(false)).newContext().newPage();
  // page.navigate("https://www.hko.gov.hk/");

  // // Stop for a moment before end =================
  // Thread.sleep(5000);

  // // Close Playwright =================
  // playwright.close();
  // }

  // @Test
  // public void sample2() throws InterruptedException {
  // Playwright playwright = Playwright.create();
  // BrowserType browserType = playwright.chrome();
  // Page page = browserType.launch(new
  // BrowserType.LaunchOptions().setHeadless(false)).newContext(
  // new Browser.NewContextOptions()
  // .setHttpCredentials("student", "teenfuture")
  // ).newPage();
  // page.navigate("https://pt-rv.teenfuture.academy/tfa/autotest/playwright/#/2");

  // Thread.sleep(8000);
  // playwright.close();
  // }

  @Test
  public void browserTest3() throws InterruptedException {
    String userDataDir = "./user-data/firefox";
    Path path = FileSystems.getDefault().getPath(userDataDir);

    System.setProperty("java.awt.headless", "false");
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) dimension.getWidth();
    int height = (int) dimension.getHeight();
    System.out.println("Screen width: " + width + ", height: " + height);

    // Start Playwright =================
    Playwright playwright = Playwright.create();
    BrowserType browserType = playwright.chromium();

    // Launch browser in non-headless mode
    BrowserContext browserContext = browserType.launchPersistentContext(path,
        new BrowserType.LaunchPersistentContextOptions().setHeadless(false));
    Page page = browserContext.newPage();

    // Navigate to the website
    page.navigate("https://www.1823.gov.hk/tc");

    for (int i = 0; i < 4; i++) {
      page.keyboard().press("PageDown", new Keyboard.PressOptions().setDelay(1000));
    }
    Thread.sleep(1000);

    // Click the chat room image
    Locator chatRoomEntranceImage = page.locator("//img[@alt='我係1823智能助理一一，你可以問我有關政府服務嘅問題。']");
    chatRoomEntranceImage.click();

    // Click the chat room text input area
    FrameLocator chatRoomTextInputFrame = page.frameLocator("//iframe[@id='ml-webchat-iframe']");
    Locator chatRoomTextInput = chatRoomTextInputFrame.locator("//input[contains(@placeholder,'問下 1823 智能助理「一一」')]");
    chatRoomTextInput.click();

    // Type a message into the chat room text input area
    chatRoomTextInput.fill("派錢未？");
    Thread.sleep(2000);
    // Click the send button
    Locator sendButton = chatRoomTextInputFrame.locator("//img[@title='傳送信息']");
    sendButton.click();

    // Type a message into the chat room text input area
    Thread.sleep(5000);

    chatRoomTextInput.fill( "我要續車牌");
    sendButton.click();
    Thread.sleep(8000);

    // Waiting
    Thread.sleep(3000);

    // Close Playwright =================
    playwright.close();
  }

}
