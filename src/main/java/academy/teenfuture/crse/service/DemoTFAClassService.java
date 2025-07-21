package academy.teenfuture.crse.service;

/* Package */
import academy.teenfuture.crse.autotestgrasp.UserAgentData;
import academy.teenfuture.crse.autotestgrasp.RecordVideo;
import academy.teenfuture.crse.repository.VideoRepository;
import academy.teenfuture.crse.modal.Video;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/* java */

@Service
public class DemoTFAClassService {

  @Autowired
  private VideoRepository videoRepository;

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

  @Value("${scrap.site7}")
  private String site7Url;

  @Value("${scrap.site8}")
  private String site8Url;

  @Value("${scrap.site9}")
  private String site9Url;

  @Value("${scrap.site10}")
  private String site10Url;

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

  public String testFast() {
    try (Playwright playwright = Playwright.create()) {
        RecordVideo recordVideo = new RecordVideo(playwright);

        Browser browser = playwright.chromium().launch();
        BrowserContext context = recordVideo.createBrowserContext();
        Page page = context.newPage();

        try {
            System.out.println("Navigating to site: " + site10Url);
            page.navigate(site10Url);
            ElementHandle weather = page.querySelector(".myText:has-text('Weather')");
            weather.click();
            Thread.sleep(50);            
            ElementHandle climate = page.querySelector(".myText:has-text('Climate')");
            climate.click();
            Thread.sleep(50); 
            ElementHandle geophysics = page.querySelector(".myText:has-text('Geophysics')");
            geophysics.click();
            Thread.sleep(50); 
            ElementHandle community = page.querySelector(".myText:has-text('Community')");
            community.click();        
            Thread.sleep(50);     
            ElementHandle learning = page.querySelector(".myText:has-text('Learning')");
            learning.click();
            Thread.sleep(50); 
            ElementHandle eServices = page.querySelector(".myText:has-text('e-Services')");
            eServices.click();
            Thread.sleep(50); 
            ElementHandle aboutUs = page.querySelector(".myText:has-text('About Us')");
            aboutUs.click();
            Thread.sleep(50); 

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

public String testSlow() {
  try (Playwright playwright = Playwright.create()) {
      RecordVideo recordVideo = new RecordVideo(playwright);

      Browser browser = playwright.chromium().launch();
      BrowserContext context = recordVideo.createBrowserContext();
      Page page = context.newPage();

      try {
          System.out.println("Navigating to site: " + site10Url);
          page.navigate(site10Url);
          ElementHandle weather = page.querySelector(".myText:has-text('Weather')");
          weather.click();            
          Thread.sleep(1000);
          ElementHandle climate = page.querySelector(".myText:has-text('Climate')");
          climate.click();
          Thread.sleep(1000);
          ElementHandle geophysics = page.querySelector(".myText:has-text('Geophysics')");
          geophysics.click();
          Thread.sleep(1000);
          ElementHandle community = page.querySelector(".myText:has-text('Community')");
          community.click();            
          Thread.sleep(1000);
          ElementHandle learning = page.querySelector(".myText:has-text('Learning')");
          learning.click();
          Thread.sleep(1000);
          ElementHandle eServices = page.querySelector(".myText:has-text('e-Services')");
          eServices.click();
          Thread.sleep(1000);
          ElementHandle aboutUs = page.querySelector(".myText:has-text('About Us')");
          aboutUs.click();
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











}