package hk1823.automation.Utility.suki.playwright;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import java.awt.Toolkit;
import java.awt.Dimension;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import java.util.Random;

import org.junit.jupiter.api.Test;
import com.microsoft.playwright.*;

public class My1823SearchTests {
  
    @Test
    public void My1823SearchTest1() throws InterruptedException {

      // Start Playwright ,use chromium browser
      Playwright playwright = Playwright.create();
      Browser browser = playwright.chromium().launch(
          new com.microsoft.playwright.BrowserType.LaunchOptions()
              .setChannel("msedge")
              .setHeadless(false)
      );
      // Set the browser window size to full screen
      Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
      int width = dimension.width;
      int height = dimension.height;
      System.out.println("width : " + width);
      System.out.println("height : " + height);
      BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(width, height));
      Page page = browserContext.newPage();
      page.navigate("https://www.1823.gov.hk/tc");
      
      page.waitForLoadState(LoadState.DOMCONTENTLOADED);

      page.locator("//span[@class='ico ico--search']").click();    
      // input search keyword
      Locator searchBar = page.locator("//div[@class='panel-body']//input[@placeholder='你想查詢什麼？']");
      searchBar.click();

      // 測試1：輸入數字
      searchBar.fill("12345");
      Locator searchBarButton = page.locator("//div[@class='panel-body']//span[@class='search-btn search-btn--lg ico ico--search']");
      searchBarButton.click();
      // 等待跳轉到搜尋頁面
      page.waitForURL(url -> url.contains("/search?q=12345"), new Page.WaitForURLOptions().setTimeout(10000));
      org.junit.jupiter.api.Assertions.assertTrue(
          page.url().contains("/search?q=12345"),
          "未正確跳轉到搜尋頁面！"
      );
      // 等待內容載入
      page.waitForSelector("body", new Page.WaitForSelectorOptions().setTimeout(5000));
      String pageContent = page.content();
      org.junit.jupiter.api.Assertions.assertTrue(
          pageContent.contains("搜尋結果"),
          "搜尋結果頁未顯示預期內容！"
      );
      // 返回首頁再測試下一個
      page.navigate("https://www.1823.gov.hk/tc");
      page.waitForLoadState(LoadState.DOMCONTENTLOADED);
      // 點擊搜尋圖標
      page.locator("//span[@class='ico ico--search']").click();
      // 等待搜尋欄可見
      page.waitForSelector("//div[@class='panel-body']//input[@placeholder='你想查詢什麼？']", new Page.WaitForSelectorOptions().setTimeout(5000));
      searchBar = page.locator("//div[@class='panel-body']//input[@placeholder='你想查詢什麼？']");
      searchBar.click(); 

      // 輸入空格
      searchBar.fill("  "); // 兩個空格
      searchBarButton.click();
      // 等待跳轉到搜尋頁面
      page.waitForURL(url -> url.contains("/search?q="), new Page.WaitForURLOptions().setTimeout(10000));
      org.junit.jupiter.api.Assertions.assertTrue(
          page.url().contains("/search?q="),
          "空格搜尋未正確跳轉到搜尋頁面！"
      );
      // 等待內容載入
      page.waitForSelector("body", new Page.WaitForSelectorOptions().setTimeout(5000));
      String pageContent2 = page.content();
      org.junit.jupiter.api.Assertions.assertTrue(
          pageContent2.contains("找不到相關結果"),
          "搜尋結果頁未顯示「找不到相關結果」！"
      );
      // 返回首頁再測試下一個
      page.navigate("https://www.1823.gov.hk/tc");
      page.waitForLoadState(LoadState.DOMCONTENTLOADED);
      // 點擊搜尋圖標
      page.locator("//span[@class='ico ico--search']").click();
      // 等待搜尋欄可見
      page.waitForSelector("//div[@class='panel-body']//input[@placeholder='你想查詢什麼？']", new Page.WaitForSelectorOptions().setTimeout(5000));
      searchBar = page.locator("//div[@class='panel-body']//input[@placeholder='你想查詢什麼？']");
      searchBar.click(); 

      // 測試3：輸入文字
      searchBar.fill("港車北上"); 
      searchBarButton = page.locator("//div[@class='panel-body']//span[@class='search-btn search-btn--lg ico ico--search']");
      searchBarButton.click();
      page.waitForSelector("//a[contains(text(),'如何申請「港珠澳大橋港車北上」（「港車北上」）？')]", new Page.WaitForSelectorOptions().setTimeout(10000));
      org.junit.jupiter.api.Assertions.assertTrue(
          page.locator("//a[contains(text(),'如何申請「港珠澳大橋港車北上」（「港車北上」）？')]").isVisible(),
          "文字搜尋未正確跳轉到搜尋頁面！"
      );

      Locator searchResult = page.locator("//a[contains(text(),'如何申請「港珠澳大橋港車北上」（「港車北上」）？')]");
      searchResult.click();

      // 模擬滑動頁面
      for (int i = 0; i < 5; i++) {
          page.keyboard().press("PageDown");
          Random random = new Random();
          int randomNumber = 4500 + random.nextInt(1500);
      }
      Thread.sleep(3000);
      
      // 點擊外部連結並切換新分頁
      Page newPage = page.waitForPopup(() -> {
      Locator link = page.locator("//a[@href='https://www.hzmbqfs.gov.hk/tc/']");
      link.click();
});
      Thread.sleep(2000);
      newPage.close();
      // 問卷按鈕流程
      page.locator("//button[@name='q_useful'][text()='是']").click();
      Thread.sleep(2000);
      page.locator("//button[@name='q_sufficient'][contains(text(),'是')]").click();
      Thread.sleep(2000);
      page.locator("//button[@name='q_easy'][contains(text(),'是')]").click();
      Thread.sleep(2000);
      page.locator("//button[contains(text(),'遞交')]").click();
      Thread.sleep(2000);
      
      // Stop for a moment before end =================
      Thread.sleep(5000);
      
      // Close Playwright =================
      browser.close();
      playwright.close();
    }
}

