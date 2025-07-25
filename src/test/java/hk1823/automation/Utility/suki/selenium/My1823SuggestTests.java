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
        // 設定 ChromeDriver 路徑（跨平台）
        // Set ChromeDriver path (cross-platform)
        String os = System.getProperty("os.name").toLowerCase();
        String driverPath;
        if (os.contains("win")) {
            driverPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "driver" + File.separator + "ChromeDriver" + File.separator + "chromedriver.exe";
        } else {
            driverPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "driver" + File.separator + "ChromeDriver" + File.separator + "chromedriver-mac-x64" + File.separator + "chromedriver";
        }
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(),'表格已成功遞交')]")));
        takeScreenshot("screenshot.png");
    }

    @Test
    public void verifyCategoryTitleConsistency() throws InterruptedException {
        // 進入首頁
        // Go to homepage
        driver.get("https://www.1823.gov.hk/tc");

        // 先點擊主菜單“要求服務/投訴”，展開子選項
        // Click main menu to expand sub-options
        By mainMenuBy = By.xpath("//span[@class='menu__text menu__text--lv1'][contains(text(),'要求服務/投訴')]");
        WebElement mainMenu = wait.until(ExpectedConditions.elementToBeClickable(mainMenuBy));
        mainMenu.click();
        Thread.sleep(500); // 稍等菜單動畫

        // 再等待目標入口卡片（公共交通服務員工及服務質素）出現
        // Wait for the target entry card to appear
        By cardBy = By.xpath("//a[@value='64a7256d88403fe3696c000000000046']");
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(cardBy));

        // 取得入口卡片的標題（img alt 屬性）
        // Get the entry card title (img alt attribute)
        WebElement img = card.findElement(By.xpath(".//img"));
        String cardTitle = img.getAttribute("alt").trim();

        // 點擊入口卡片
        // Click the entry card
        card.click();

        // 等待表單標題出現
        // Wait for the form title to appear
        By formTitleBy = By.xpath("//p[@class='form-theme__name']");
        WebElement formTitleElem = wait.until(ExpectedConditions.visibilityOfElementLocated(formTitleBy));
        String formTitle = formTitleElem.getText().trim();

        // 輸出比對結果（美化格式，帶外框，中英文）
        // Print comparison result (pretty format with border, Chinese & English)
        String border = "==============================";
        System.out.println("\n" + border);
        System.out.println("【入口卡片標題 (img alt) / Entry Card Title (img alt)】\n  " + cardTitle);
        System.out.println("------------------------------");
        System.out.println("【表單標題 / Form Title】\n  " + formTitle);
        System.out.println(border + "\n");

        // 斷言兩者一致（忽略大小寫與空白）
        // Assert both titles are equal (ignore case and whitespace)
        org.junit.jupiter.api.Assertions.assertTrue(
            cardTitle.replaceAll("\\s+", "").equalsIgnoreCase(formTitle.replaceAll("\\s+", "")),
            "入口卡片標題與表單標題不一致 (Entry card title and form title are not consistent)"
        );
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
    // Take screenshot
    private void takeScreenshot(String baseFileName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            // 保存到 testresult 目錄，檔名加上時間戳
            // Save to testresult directory, filename with timestamp
            String screenshotDir = "testresult";
            File dir = new File(screenshotDir);
            if (!dir.exists()) dir.mkdirs();
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String screenshotPath = screenshotDir + File.separator + baseFileName.replace(".png", "") + "_" + timestamp + ".png";
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            System.out.println("Screenshot saved as " + screenshotPath);
        } catch (IOException e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }

    // 生成帶時間戳的 index.html 路徑（如有自動生成報告，請用此方法）
    // Generate index.html path with timestamp (use this for report generation)
    private String getTimestampedReportPath() {
        String reportDir = "testresult";
        File dir = new File(reportDir);
        if (!dir.exists()) dir.mkdirs();
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        return reportDir + File.separator + "index_" + timestamp + ".html";
    }
}