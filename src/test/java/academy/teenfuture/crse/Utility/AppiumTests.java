package academy.teenfuture.crse.Utility;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.File;
import java.time.Duration;
import java.lang.Thread;
import java.lang.InterruptedException;

public class AppiumTests {

    @Test
    public void startTest_way1() throws InterruptedException, URISyntaxException {

        // Invoke the appium server ====================================
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        serviceBuilder.withAppiumJS(

                new File("/Users/robintsang/.nvm/versions/node/v22.17.0/bin/appium"))
                // !!!!!! 1) ===> locate your appium installation location !!!!!!

                .withIPAddress("127.0.0.1").usingPort(4723)
                .withTimeout(Duration.ofSeconds(200));

        AppiumDriverLocalService service = serviceBuilder.build();
        service.start();

        // Invoke the appium-inspector ====================================
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android")
                .setDeviceName("emulator-5554")
                .setAutomationName("UIAutomator2")
                // .setAppWaitForLaunch(false)

                //.setApp(System.getProperty("user.dir") + "/Users/robintsang/Downloads/TeenFuture/JavaCode/springDemo/src/main/resources/apk/ApiDemos-debug.apk");
                .setApp("/Users/robintsang/Downloads/TeenFuture/JavaCode/springDemo/src/main/resources/apk/ApiDemos-debug.apk");
        // !!!!!! 2) ===> set your apk location !!!!!!

        try {
            AndroidDriver driver = new AndroidDriver(new URI("http://127.0.0.1:4723/").toURL(), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            Thread.sleep(5000);

            // do something at this point

            driver.quit();
            service.stop();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
}
