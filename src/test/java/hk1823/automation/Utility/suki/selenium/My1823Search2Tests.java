package hk1823.automation.Utility.suki.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;

public class My1823Search2Tests {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // EdgeDriver 
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        driver = new EdgeDriver(options);

        // 設定全螢幕
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(screenSize.width, screenSize.height));

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void my1823Search2Test() throws InterruptedException {
        driver.get("https://www.1823.gov.hk/tc");

        // 點擊搜尋按鈕
        WebElement searchIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='ico ico--search']")));
        searchIcon.click();

        // 輸入搜尋關鍵字
        WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='panel-body']//input[@placeholder='你想查詢什麼？']")));
        searchBar.click();
        searchBar.sendKeys("港車北上");

        // 點擊搜尋按鈕
        WebElement searchBtn = driver.findElement(By.xpath("//div[@class='panel-body']//span[@class='search-btn search-btn--lg ico ico--search']"));
        searchBtn.click();

        // 等待搜尋結果出現並斷言
        WebElement resultLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(text(),'如何申請「港珠澳大橋港車北上」（「港車北上」）？')]")));
        Assertions.assertTrue(resultLink.isDisplayed(), "搜尋結果連結未正確顯示！");

        resultLink.click();

        // 模擬滑動頁面
        for (int i = 0; i < 8; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, window.innerHeight/2);");
            Thread.sleep(700 + (int) (Math.random() * 700));
        }
        Thread.sleep(5000);

        // 點擊外部連結並切換新分頁
        String originalWindow = driver.getWindowHandle();
        WebElement externalLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='https://www.hzmbqfs.gov.hk/tc/']")));
        externalLink.click();

        // 等待新分頁開啟並切換
        wait.until(driver -> driver.getWindowHandles().size() > 1);
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        Thread.sleep(2000);
        driver.close();
        driver.switchTo().window(originalWindow);

        // 問卷按鈕流程
        WebElement btnUseful = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@name='q_useful'][text()='是']")));
        btnUseful.click();
        Thread.sleep(2000);

        WebElement btnSufficient = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@name='q_sufficient'][contains(text(),'是')]")));
        btnSufficient.click();
        Thread.sleep(2000);

        WebElement btnEasy = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@name='q_easy'][contains(text(),'是')]")));
        btnEasy.click();
        Thread.sleep(2000);

        WebElement btnSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'遞交')]")));
        btnSubmit.click();
        Thread.sleep(2000);

        // Stop for a moment before end =================
        Thread.sleep(5000);
    }
} 