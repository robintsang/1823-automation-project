package hk1823.automation.Utility;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.File;
import org.apache.commons.io.FileUtils;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;


public class My1823SuggestTests {
    @Test
    public void getStart() throws InterruptedException {
        // should set your driver path in String, eg.
       System.setProperty("webdriver.chrome.driver",
       System.getProperty("user.dir") + "\\src\\main\\resources\\driver\\ChromeDriver\\chromedriver.exe"
      );
      WebDriver driver = new ChromeDriver();
        driver.get("https://www.1823.gov.hk/tc");

        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='menu__text menu__text--lv1'][contains(text(),'要求服務/投訴')]"))).click();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@value='64a7256d88403fe3696c000000000046']"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'巴士')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'下一步')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'九巴')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'下一步')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'起點站及／或終點站（請註明）')]"))).click();
        WebElement input_text = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='3db274fa79ba43f4869bf3fb912e9ed2_0']")));
        input_text.sendKeys("啓業邨站");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'下一步')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'其他（請註明）')]"))).click();
        WebElement input_text2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='6494d777192a40a4af38245004311acc_0']")));
        input_text2.sendKeys("司機態度甚差");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'下一步')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'沒有')]"))).click();
        WebElement input_text3 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@id='個案補充資料']")));
        input_text3.sendKeys("7月12日下午搭車時詢問司機具體落車地點，司機態度差，不理會並表現不耐煩狀，請跟進");
        WebElement input_suggest = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='suggest']")));
        input_suggest.sendKeys("啓業邨");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'下一步')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='agree_1823_2']//span[contains(text(),'不同意')]"))).click();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'下一步')]"))).click();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'遞交')]"))).click();
        
        // Take screenshot and save as file
         try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotDir = "QA tester course" + File.separator + "project2：1823";
            String screenshotPath = "D:" + File.separator + screenshotDir + File.separator + "screenshot.png";
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            System.out.println("截图已保存为 screenshot.png");
        } catch (IOException e) {
            System.out.println("截图失败: " + e.getMessage());
        } finally {
        driver.close();
    }
    }
}