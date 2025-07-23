package hk1823.automation.Utility.suki.selenium;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class My1823SuggestTests {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "\\src\\main\\resources\\driver\\ChromeDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getStart() throws InterruptedException {
        driver.get("https://www.1823.gov.hk/tc");

        click(By.xpath("//span[@class='menu__text menu__text--lv1'][contains(text(),'要求服務/投訴')]"));
        scrollToBottom();
        click(By.xpath("//a[@value='64a7256d88403fe3696c000000000046']"));

        click(By.xpath("//span[contains(text(),'巴士')]"));
        click(By.xpath("//button[contains(text(),'下一步')]"));
        click(By.xpath("//span[contains(text(),'九巴')]"));
        click(By.xpath("//button[contains(text(),'下一步')]"));
        click(By.xpath("//span[contains(text(),'起點站及／或終點站（請註明）')]"));
        fill(By.xpath("//input[@id='3db274fa79ba43f4869bf3fb912e9ed2_0']"), "啓業邨站");
        click(By.xpath("//button[contains(text(),'下一步')]"));
        click(By.xpath("//span[contains(text(),'其他（請註明）')]"));
        fill(By.xpath("//input[@id='6494d777192a40a4af38245004311acc_0']"), "司機態度甚差");
        click(By.xpath("//button[contains(text(),'下一步')]"));
        click(By.xpath("//span[contains(text(),'沒有')]"));
        fill(By.xpath("//textarea[@id='個案補充資料']"), "7月12日下午搭車時詢問司機具體落車地點，司機態度差，不理會並表現不耐煩狀，請跟進");
        fill(By.xpath("//input[@id='suggest']"), "啓業邨");
        click(By.xpath("//button[contains(text(),'下一步')]"));
        click(By.xpath("//label[@for='agree_1823_2']//span[contains(text(),'不同意')]"));
        scrollToBottom();
        click(By.xpath("//button[contains(text(),'下一步')]"));
        scrollToBottom();
        click(By.xpath("//button[contains(text(),'遞交')]"));

        takeScreenshot("screenshot.png");
    }

    // 點擊操作
    private void click(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    // 填寫操作
    private void fill(By by, String text) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
        element.clear();
        element.sendKeys(text);
    }

    // 滾動到底部
    private void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try {
            Thread.sleep(1000); // 可視情況調整
        } catch (InterruptedException ignored) {}
    }

    // 截圖
    private void takeScreenshot(String fileName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotDir = "QA tester course" + File.separator + "project2：1823";
            String screenshotPath = "D:" + File.separator + screenshotDir + File.separator + fileName;
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            System.out.println("Screenshot saved as " + screenshotPath);
        } catch (IOException e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }
}