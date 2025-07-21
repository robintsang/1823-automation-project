package academy.teenfuture.crse.utility;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
  import org.openqa.selenium.safari.SafariDriver;


public class SeleniumTests {
    // @Test
    // public void getStart() throws InterruptedException {
    //     System.setProperty("webdriver.chrome.driver",
    //             // should set your driver path in String, eg.
    //             // "C://AutoTestGraspScrapeCrawl//selenium//driver//chromedriver.exe"
    //             System.getProperty("user.dir") +
    //             // "\\src\\main\\resources\\driver\\ChromeDriver\\chromedriver.exe"
    //             // for Mac: System.getProperty("user.dir") +
    //             // "/src/main/resources/driver/msedgedriver/msedgedriver"
    //                     "/src/main/resources/driver/ChromeDriver/chromedriver-mac-x64/chromedriver");
    //     WebDriver driver = new ChromeDriver();
    //     driver.get("https://www.google.com.hk");

    //     // Stop for a moment before end =================
    //     Thread.sleep(8000);

    //     driver.close();
    // }

    @Test
    public void getStart() throws InterruptedException {
        WebDriver driver = new SafariDriver();
        driver.get("https://www.google.com.hk");
        Thread.sleep(3000);
        driver.close();
    }
}
