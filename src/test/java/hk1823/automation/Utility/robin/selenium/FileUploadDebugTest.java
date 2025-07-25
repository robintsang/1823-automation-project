package hk1823.automation.Utility.robin.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.io.File;

/**
 * 演示为什么Selenium在1823.gov.hk上使用sendKeys()上传文件可能失败的测试类
 * 以及相应的解决方案
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileUploadDebugTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArgument("--disable-web-security");
        options.addArgument("--allow-running-insecure-content");
        options.addArgument("--disable-extensions");
        
        // 跨平台驱动路径设置
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
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void demonstrateFileUploadIssue() throws InterruptedException {
        System.out.println("=== 演示1823.gov.hk文件上传问题 ===");
        
        // 1. 导航到投诉页面
        driver.get("https://www.1823.gov.hk/en/");
        
        // 2. 点击投诉链接
        WebElement complaintBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/form/complain') and contains(@class, 'menu__link--lv1')]")));
        complaintBtn.click();

        // 3. 选择"Others"类别
        WebElement otherCategory = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@value='64a7256d88403fe3696c000000000151']")));
        otherCategory.click();

        // 4. 选择"No"选项
        WebElement noOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@value='No']")));
        noOption.click();

        // 5. 填写基本信息
        WebElement caseInfoTextarea = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//textarea[contains(@id, 'caseInfo')]")));
        caseInfoTextarea.sendKeys("Test file upload issue");

        WebElement locationInput = driver.findElement(By.xpath("//input[contains(@id, 'location')]"));
        locationInput.sendKeys("Test Location");

        // 6. 点击Next按钮
        WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-primary') and contains(text(), 'Next')]")));
        nextBtn.click();

        // 等待页面加载
        Thread.sleep(3000);

        // 7. 尝试定位文件上传元素
        try {
            System.out.println("=== 尝试定位文件上传元素 ===");
            
            // 方式1: 通过ID定位
            WebElement fileInput1 = null;
            try {
                fileInput1 = driver.findElement(By.xpath("//input[contains(@id,'fileupload') and @type='file']"));
                System.out.println("✓ 成功通过ID定位到文件上传元素: " + fileInput1.getTagName());
                System.out.println("  - 元素可见: " + fileInput1.isDisplayed());
                System.out.println("  - 元素启用: " + fileInput1.isEnabled());
                System.out.println("  - 元素属性: " + fileInput1.getAttribute("outerHTML"));
            } catch (NoSuchElementException e) {
                System.out.println("✗ 无法通过ID定位文件上传元素");
            }

            // 方式2: 通过type属性定位
            try {
                WebElement fileInput2 = driver.findElement(By.xpath("//input[@type='file']"));
                System.out.println("✓ 成功通过type属性定位到文件上传元素");
            } catch (NoSuchElementException e) {
                System.out.println("✗ 无法通过type属性定位文件上传元素");
            }

            // 8. 分析为什么sendKeys可能失败
            if (fileInput1 != null) {
                System.out.println("\n=== 分析文件上传失败的可能原因 ===");
                
                // 检查元素是否被隐藏
                String display = fileInput1.getCssValue("display");
                String visibility = fileInput1.getCssValue("visibility");
                String opacity = fileInput1.getCssValue("opacity");
                
                System.out.println("CSS属性分析:");
                System.out.println("  - display: " + display);
                System.out.println("  - visibility: " + visibility);
                System.out.println("  - opacity: " + opacity);
                
                // 检查是否有JavaScript事件监听器
                String onclick = fileInput1.getAttribute("onclick");
                String onchange = fileInput1.getAttribute("onchange");
                System.out.println("事件监听器:");
                System.out.println("  - onclick: " + onclick);
                System.out.println("  - onchange: " + onchange);
                
                // 9. 尝试不同的文件上传方法
                String filePath = System.getProperty("user.dir") + "/test_uploads/fu_yip_street_flood_image.jpg";
                File testFile = new File(filePath);
                
                if (testFile.exists()) {
                    System.out.println("\n=== 尝试不同的文件上传方法 ===");
                    
                    // 方法1: 直接使用sendKeys
                    try {
                        System.out.println("方法1: 直接使用sendKeys...");
                        fileInput1.sendKeys(filePath);
                        Thread.sleep(2000);
                        
                        // 检查文件是否被选中
                        String value = fileInput1.getAttribute("value");
                        if (value != null && !value.isEmpty()) {
                            System.out.println("✓ sendKeys成功，文件路径: " + value);
                        } else {
                            System.out.println("✗ sendKeys失败，文件未被选中");
                        }
                    } catch (Exception e) {
                        System.out.println("✗ sendKeys方法失败: " + e.getMessage());
                    }
                    
                    // 方法2: 使用JavaScript设置文件
                    try {
                        System.out.println("\n方法2: 使用JavaScript设置文件...");
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        
                        // 注意：这种方法通常不会工作，因为浏览器安全限制
                        js.executeScript("arguments[0].value = arguments[1];", fileInput1, filePath);
                        System.out.println("JavaScript方法执行完成");
                        
                        String jsValue = fileInput1.getAttribute("value");
                        if (jsValue != null && !jsValue.isEmpty()) {
                            System.out.println("✓ JavaScript方法成功");
                        } else {
                            System.out.println("✗ JavaScript方法失败（预期结果，由于安全限制）");
                        }
                    } catch (Exception e) {
                        System.out.println("✗ JavaScript方法失败: " + e.getMessage());
                    }
                    
                } else {
                    System.out.println("✗ 测试文件不存在: " + filePath);
                }
            }

        } catch (Exception e) {
            System.out.println("测试过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== 文件上传问题分析完成 ===");
    }

    @Test
    public void demonstrateWorkaroundSolutions() {
        System.out.println("\n=== 文件上传问题的解决方案 ===");
        
        System.out.println("1. 使用Playwright (推荐):");
        System.out.println("   - Playwright的setInputFiles()方法专门为文件上传设计");
        System.out.println("   - 可以处理隐藏的文件输入元素");
        System.out.println("   - 支持多文件上传");
        System.out.println("   - 代码示例: page.setInputFiles(\"input[type=file]\", filePath);");
        
        System.out.println("\n2. 使用Robot类 (Selenium替代方案):");
        System.out.println("   - 模拟键盘和鼠标操作");
        System.out.println("   - 需要先点击文件选择按钮");
        System.out.println("   - 平台相关，需要不同的实现");
        
        System.out.println("\n3. 使用AutoIT (Windows专用):");
        System.out.println("   - 处理Windows文件对话框");
        System.out.println("   - 需要额外的AutoIT脚本");
        
        System.out.println("\n4. 检查网站的文件上传实现:");
        System.out.println("   - 某些网站使用自定义文件上传组件");
        System.out.println("   - 可能需要特殊的交互方式");
        System.out.println("   - 文件输入元素可能被CSS隐藏");
        
        System.out.println("\n5. 使用Selenium的Actions类:");
        System.out.println("   - 结合JavaScript执行器");
        System.out.println("   - 先使元素可见，再使用sendKeys");
    }
}