package academy.teenfuture.crse.utility_temp;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.lang.Thread;
import java.lang.InterruptedException;

public class SimpleAppiumTest {

    @Test
    public void simpleTest() throws InterruptedException, URISyntaxException {
        
        // Set Android SDK environment variables
        System.setProperty("ANDROID_HOME", "/Users/robintsang/Library/Android/sdk");
        System.setProperty("ANDROID_SDK_ROOT", "/Users/robintsang/Library/Android/sdk");

        // Create capabilities
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android")
                .setDeviceName("emulator-5554")
                .setAutomationName("UIAutomator2")
                .setApp(System.getProperty("user.dir") + "/src/main/resources/apkApiDemos-debug.apk");

        try {
            // Connect to Appium server (make sure it's running on port 4723)
            AndroidDriver driver = new AndroidDriver(new URI("http://127.0.0.1:4723").toURL(), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            System.out.println("App launched successfully!");
            Thread.sleep(5000);

            // do something at this point
            System.out.println("Current activity: " + driver.currentActivity());

            driver.quit();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
} 