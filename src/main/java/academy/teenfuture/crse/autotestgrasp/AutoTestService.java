package academy.teenfuture.crse.autotestgrasp;

/* Package */
import academy.teenfuture.crse.autotestgrasp.UserAgentData;
import academy.teenfuture.crse.autotestgrasp.RecordVideo;
import academy.teenfuture.crse.autotestgrasp.ImgRepository;
import academy.teenfuture.crse.autotestgrasp.ImgData;
/* Package */

/* springframework */
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
/* springframework */

import org.apache.commons.io.FileUtils;

/* playwright */
import com.microsoft.playwright.*;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.*;

/* playwright */

/* selenium */
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
/* selenium */

/* java */
import static java.util.Arrays.asList;
import java.util.regex.Pattern;
import java.util.Random;
import java.util.Map;
import java.util.Arrays;
import javax.security.sasl.AuthenticationException;
import java.util.Optional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Duration;
import java.io.ByteArrayOutputStream;
import java.time.Duration;

/* java */


@Service
public class AutoTestService {

  private final ImgRepository imgRepository;

  public AutoTestService(ImgRepository imgRepository) {
      this.imgRepository = imgRepository;
  }

  @Value("${scrap.site1}")
  private String site1Url;
  
  @Value("${scrap.site2}")
  private String site2Url;

  @Value("${scrap.site3}")
  private String site3Url;

  @Value("${scrap.site4}")
  private String site4Url;

  @Value("${scrap.site5}")
  private String site5Url;

  @Value("${scrap.site6}")
  private String site6Url;

  private UserAgentData userAgentData;
  
  public String getHelloMessage() {
    return "Hello";
  }

  public void callTest() {
    System.out.println("This is a test message in Java print line");
  } 

  public void deleteFilesInPath() {
    // 建立要刪除的目錄物件
    String path = "videos"; // 將路徑設置為 "videos" 的路徑

    // 建立要刪除的目錄物件
    File directory = new File(path);

    // 確認目錄存在並是一個目錄
    if (directory.exists() && directory.isDirectory()) {
        // 取得目錄中的所有檔案
        File[] files = directory.listFiles();

        // 刪除每個檔案
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
                System.out.println("已刪除檔案: " + file.getName());
            }
        }

        System.out.println("所有檔案已刪除");
    } else {
        System.out.println("路徑不存在或不是一個目錄");
    }
  }

  public void deleteAll() {
  // 設定要刪除的目錄路徑
  String path = "videos"; // 將路徑設置為 "videos" 的路徑

  // 建立要刪除的目錄物件
  File directory = new File(path);

  // 確認目錄存在並是一個目錄
  if (directory.exists() && directory.isDirectory()) {
    try {
      // 使用Apache Commons IO庫來刪除目錄中的所有內容
      File[] files = directory.listFiles();
      for (File file : files) {
        if (file.isFile()) {
          String fileName = file.getName();
          FileUtils.forceDelete(file);
          System.out.println("已刪除檔案: " + fileName);
        }
      }
      System.out.println("已刪除目錄中的所有內容");
    } catch (IOException e) {
      System.out.println("無法刪除目錄中的內容: " + e.getMessage());
    }
  } else {
    System.out.println("路徑不存在或不是一個目錄");
  }
  }


/* playwright */

public String runningTests() {
  try (Playwright playwright = Playwright.create()) {
      RecordVideo recordVideo = new RecordVideo(playwright);

      Browser browser = playwright.chromium().launch();
      BrowserContext context = recordVideo.createBrowserContext();
      Page page = context.newPage();

      try {
          System.out.println("Navigating to site: " + site5Url);
          page.navigate(site5Url);
          System.out.println("Clicking on search input");
          page.locator("input[name=\"search\"]").click();
          Thread.sleep(1000);
          System.out.println("Filling search input with 'playwright'");
          page.locator("input[name=\"search\"]").fill("playwright");
          Thread.sleep(1000);
          System.out.println("Pressing Enter");
          page.locator("input[name=\"search\"]").press("Enter");
          Thread.sleep(1000);

          return "Tested Website: " + page.url();
      } catch (Exception e) {
          System.out.println("Navigation failed: " + e.getMessage());
          return "Navigation failed: " + e.getMessage();
      } finally {
          System.out.println(page.video().path());
          // Clean up resources
          page.close();
          context.close();
          browser.close();
          playwright.close();
      }
  } catch (Exception e) {
      // Handle the exception
      e.printStackTrace();
      return "An error occurred during test execution.";
  }
}

  public String tracingBrowser() {
    try (Playwright playwright = Playwright.create()) {
    RecordVideo recordVideo = new RecordVideo(playwright);

    Browser browser = playwright.chromium().launch();
    BrowserContext context = recordVideo.createBrowserContext();
    
    // Start tracing before creating / navigating a page.
    context.tracing().start(new Tracing.StartOptions()
      .setScreenshots(true)
      .setSnapshots(true)
      .setSources(true));
    
    Page page = context.newPage();
    page.navigate(site3Url);
    
    // Stop tracing and export it into a zip archive.
    context.tracing().stop(new Tracing.StopOptions()
      .setPath(Paths.get("trace.zip")));
      page.close();
      context.close();
      browser.close();

      return "tracing completed successfully!";
  } catch (Exception e) {
    throw new RuntimeException("An error occurred during tracing: " + e.getMessage(), e);
  } 
}
 
  
  public String launchBrowser() {
      System.out.println("Launching browser");

      try (Playwright playwright = Playwright.create()) {
          
          RecordVideo recordVideo = new RecordVideo(playwright);
          
          Browser browser = playwright.chromium().launch();
          BrowserContext context = recordVideo.createBrowserContext();
          Page page = context.newPage();
          
          try {
              page.navigate(site3Url);
              System.out.println(site3Url);
              System.out.println(page.title());
              return page.title();
              
          } catch (PlaywrightException e) {
              return "Navigation failed: " + e.getMessage();
          }finally {
          // Close the page, context, and browser
          System.out.println("Testing Finished.Closing The Browser");
          page.close();
          context.close();
          browser.close();
          System.out.println("Browser Closed");
        }
    } catch (PlaywrightException e) {
      return "Error creating or closing browser: " + e.getMessage();
    }
  }

  public void screenshotPlaywirght() {
    System.out.println("This is remind message of screenshot web-page");
    try (Playwright playwright = Playwright.create()) {
        String filePath = "PlaywirghtSiteScreenshot1.png";
        RecordVideo recordVideo = new RecordVideo(playwright);

        Browser browser = playwright.chromium().launch();
        BrowserContext context = recordVideo.createBrowserContext();
        /*For hiding the headless chrome*/
        Page page = context.newPage();
        page.navigate(site1Url);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));

        // 关闭Playwright相关实例
        page.close();
        context.close();
        browser.close();
        playwright.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
  
  public String runAutomation() {
    try (Playwright playwright = Playwright.create()) {
      RecordVideo recordVideo = new RecordVideo(playwright);
          
      Browser browser = playwright.chromium().launch();
      BrowserContext context = recordVideo.createBrowserContext();
        
        Page page = context.newPage();

        page.navigate(site2Url);
        System.out.println("Navigated to website");

        assertThat(page).hasTitle(Pattern.compile("Playwright"));
        System.out.println("Page title verifying");

        Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));        
        System.out.println("Located 'Get Started' link.");

        assertThat(getStarted).hasAttribute("href", "/docs/intro");
        System.out.println("'href' attribute verified.");

        System.out.println("Clicking on 'Get Started' link");
        getStarted.click();

        System.out.println("Verifying heading 'Installation'...");
        assertThat(page.getByRole(AriaRole.HEADING,
           new Page.GetByRoleOptions().setName("Installation"))).isVisible();

        System.out.println("Automation completed successfully!");

        return "Automation completed successfully!";
    } catch (Exception e) {
        throw new RuntimeException("An error occurred during automation: " + e.getMessage(), e);
    }
  }

  public void requestResponse() {
    try (Playwright playwright = Playwright.create()) {
      RecordVideo recordVideo = new RecordVideo(playwright);
          
      Browser browser = playwright.chromium().launch();
      BrowserContext context = recordVideo.createBrowserContext();
 
      Page page = context.newPage();
      page.onRequest(request -> System.out.println(">> " + request.method() + " " + request.url()));
      page.onResponse(response -> System.out.println("<<" + response.status() + " " + response.url()));
      page.navigate(site1Url);
      browser.close();
    } catch (Exception e) {
      throw new RuntimeException("An error occurred: " + e.getMessage());
    }
  }
      
  public String loginBrowser() {
    System.out.println("Launching browser");

    try (Playwright playwright = Playwright.create()) {
      RecordVideo recordVideo = new RecordVideo(playwright);
          
      Browser browser = playwright.chromium().launch();
      BrowserContext context = recordVideo.createBrowserContext();
      Page page = context.newPage();

        try {
            page.navigate(site3Url);
            System.out.println(site3Url);
            System.out.println(page.title());
            // Interact with login form
            try {
                Thread.sleep(3000);
                page.getByLabel("Username or email address").fill("it1");
                // Add delay
                Thread.sleep(3000);
                page.getByLabel("Password").fill("shifang1");
                // Add delay
                Thread.sleep(3000);
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
                System.out.println("登入成功");
                System.out.println("網頁標題：" + page.title());
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println("登入失敗" + e.getMessage());
            }
            return page.title();
        } catch (PlaywrightException e) {
            return "Navigation failed: " + e.getMessage();
        } finally {
            // Close the page, context, and browser
            System.out.println(page.video().path());
            page.close();
            context.close();
            browser.close();
            
        }
    } catch (PlaywrightException e) {
        return "Error creating or closing browser: " + e.getMessage();
    }
  }

  public String loginGoogle() {
    System.out.println("Launching browser");

    try (Playwright playwright = Playwright.create()) {
      RecordVideo recordVideo = new RecordVideo(playwright);

      Browser browser = playwright.chromium().launch();
      BrowserContext context = recordVideo.createBrowserContext();
      Page page = context.newPage();

        try {
            page.navigate(site4Url);
            System.out.println(site4Url);
            System.out.println(page.title());
            
            // Interact with login form
            try {
                System.out.println(page.video().path());
                Thread.sleep(3000);
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue with Google")).click();
                Thread.sleep(3000);
                System.out.println("網頁標題：" + page.title());
                page.waitForSelector("#identifierId");

                ElementHandle element = page.querySelector("#identifierId");
                if (element != null) {
                    System.out.println("網頁中存在identifierId");
                } else {
                    System.out.println("網頁中不存在identifierId");                  
                } 

                page.fill("#identifierId", "tftommyktest@gmail.com");
                page.waitForSelector("#identifierNext");
                page.click("#identifierNext");
                Thread.sleep(3000); 
              /*  
                Since Google has the function of authenticating human and machine login, the automatic login function cannot be realized by using
                But Still keeping these code for review 
              */
            } catch (Exception e) {
                System.out.println("失敗" + e.getMessage());
            }
            return page.title();
        } catch (PlaywrightException e) {
            return "Navigation failed: " + e.getMessage();
        } finally {
            // Close the page, context, and browser
            page.close();
            context.close();
            browser.close();
            
        }
    } catch (PlaywrightException e) {
        return "Error creating or closing browser: " + e.getMessage();
    }
  }
/* playwright */

 

/* selenium */
    public String performAutomatedTest() {
      this.userAgentData = new UserAgentData();
 
      WebDriverManager.chromedriver().setup();      
    	ChromeOptions options = new ChromeOptions();
      String randomUserAgent = userAgentData.getRandomUserAgent();
      options.addArguments("--user-agent=" + randomUserAgent);
    	options.addArguments("--headless=new");
      options.addArguments("--no-sandbox");
      options.addArguments("--disable-dev-shm-usage");
      options.addArguments("--remote-allow-origins=*");
      WebDriver driver = new ChromeDriver(options);
      //driver.get(site6Url);
      driver.get(site1Url);
      String title = driver.getTitle();
      System.out.println(title);
      WebElement webTitle = driver.findElement(By.xpath("//*[contains(text(), 'TFA Full Stack Programme')]"));
      String webTitleText = webTitle.getText();

      WebElement sessionTitle = driver.findElement(By.xpath("//*[contains(text(), 'Welcome to our Coding Course')]"));
      String sessionTitleText = sessionTitle.getText();

      System.out.println(webTitleText);
      System.out.println(sessionTitleText);

      WebElement navBarButton = driver.findElement(By.cssSelector("button"));
      navBarButton.click();

      WebElement javaCourse = driver.findElement(By.xpath("//span[contains(@class, 'MuiTypography-root') and contains(text(), 'Java')]"));
      String javaCourseText = javaCourse.getText();

      System.out.println("Selected Course:" + javaCourseText);
      
      javaCourse.click();

      WebElement javaCourseTitle = driver.findElement(By.xpath("//h4[contains(@class, 'MuiTypography-root') and text()='Java']"));
      String javaCourseTitleText = javaCourseTitle.getText();
      System.out.println("Course Text:" + javaCourseTitleText);
      
      String value = (title);
      driver.quit();

      return value;
    }

    public void screenshotSelenium() {
      try {
          this.userAgentData = new UserAgentData();
  
          WebDriverManager.chromedriver().setup();
          ChromeOptions options = new ChromeOptions();
          String randomUserAgent = userAgentData.getRandomUserAgent();
          options.addArguments("--user-agent=" + randomUserAgent);
          options.addArguments("--headless=new");
          options.addArguments("--no-sandbox");
          options.addArguments("--disable-dev-shm-usage");
          options.addArguments("--remote-allow-origins=*");
          WebDriver driver = new ChromeDriver(options);
  
          driver.get(site1Url);
          File siteScreenshot1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
          FileHandler.copy(siteScreenshot1, new File("./SeleniumSiteScreenshot1.png"));
  
          WebElement webTitle = driver.findElement(By.xpath("//*[contains(text(), 'TFA Full Stack Programme')]"));
          String webTitleText = webTitle.getText();
  
          WebElement sessionTitle = driver.findElement(By.xpath("//*[contains(text(), 'Welcome to our Coding Course')]"));
          String sessionTitleText = sessionTitle.getText();
  
          WebElement navBarButton = driver.findElement(By.cssSelector("button"));
          navBarButton.click();
  
          WebElement javaCourse = driver.findElement(By.xpath("//span[contains(@class, 'MuiTypography-root') and contains(text(), 'Java')]"));
          String javaCourseText = javaCourse.getText();
  
          System.out.println("Selected Course: " + javaCourseText);
  
          javaCourse.click();
  
          File siteScreenshot2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
          FileHandler.copy(siteScreenshot2, new File("./SeleniumSiteScreenshot2.png"));
          driver.quit();
      } catch (Exception e) {
         
          e.printStackTrace();
      }
  }
/* selenium */



/*Return img*/
public void screenshotSeleniumImg() {
    try {
        this.userAgentData = new UserAgentData();

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        String randomUserAgent = userAgentData.getRandomUserAgent();
        options.addArguments("--user-agent=" + randomUserAgent);
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);

        driver.get(site1Url);
      
      // 使用WebDriverWait等待元素可点击

      Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
      .withTimeout(Duration.ofSeconds(30))
      .pollingEvery(Duration.ofSeconds(5))
      .ignoring(NoSuchElementException.class);
      
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        File siteScreenshot1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(siteScreenshot1.toPath(), outputStream);
        byte[] screenshotData = outputStream.toByteArray();

      ImgData imgData = new ImgData();
      imgData.setImg(screenshotData);
      imgRepository.save(imgData);
  
        driver.quit();
    } catch (Exception e) {
        e.printStackTrace();
  }
  }

public byte[] getImgData() {
  ImgData imgData = imgRepository.findById(1L).orElse(null); // 根据需要的图像数据的ID进行查询
  if (imgData != null) {
      return imgData.getImg();
  }
  return null;
}
/*public byte[] screenshotPlaywrightImg() {
  System.out.println("This is remind message of screenshot web-page");
  try (Playwright playwright = Playwright.create()) {
      String filePath = "PlaywirghtSiteScreenshotReturn.png";
      RecordVideo recordVideo = new RecordVideo(playwright);

      Browser browser = playwright.chromium().launch();
      BrowserContext context = recordVideo.createBrowserContext();
      /*For hiding the headless chrome*/
/*      Page page = context.newPage();
      page.navigate(site1Url);
      byte[] screenshotBytes = page.screenshotAsBytes();

      
      page.close();
      context.close();
      browser.close();
      playwright.close();

      return screenshotBytes;
  } catch (Exception e) {
      e.printStackTrace();
      return null;
  }
}*/
/*Return img*/

}