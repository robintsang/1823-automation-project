package academy.teenfuture.crse.service;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.Optional;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.regex.Pattern;
import java.util.*;
import com.microsoft.playwright.Route.FulfillOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import java.util.function.Consumer;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;

@Service
public class PlayWrightService01 {


  public void callTest() {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
        .setHeadless(true));
      BrowserContext context = browser.newContext(new Browser.NewContextOptions()
      .setUserAgent("Hi I am Lawrence"));
      Page page = context.newPage();
      page.navigate("https://tfa-crse.teenfuture.academy/");
      /*page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign in")).click();
      page.getByLabel("Username or email address").click();
      page.getByLabel("Username or email address").fill("kwanc.law@gmail.com");
      page.getByLabel("Password").click();
      page.getByLabel("Password").fill("Abcd1989@#");
      page.getByLabel("Password").press("Enter");*/

      //context.storageState(new BrowserContext.StorageStateOptions().setPath("auth.json"));
      BrowserContext context2 = browser.newContext(new Browser.NewContextOptions()
      .setGeolocation(41.890221, 12.492348)
      .setPermissions(Arrays.asList("geolocation")));

      context2.setGeolocation(new Geolocation(48.858455, 2.294474));

      // A primitive value.
      page.evaluate("num => num", 42);

      // An array.
      page.evaluate("array => array.length", Arrays.asList(1, 2, 3));

      // An object.
      Map<String, Object> obj = new HashMap<>();
      obj.put("foo", "bar");
      page.evaluate("object => object.foo", obj);

      Request request = page.waitForRequest("**/*logo*.png", () -> {
        page.navigate("https://wikipedia.org");
      });
      System.out.println(request.url());
      /*Page popup = page.waitForPopup(() -> {
        page.getByText("open the popup").click();
      });
      popup.navigate("https://wikipedia.org");*/
      page.onRequest(request2 -> System.out.println("Request sent: " + request2.url()));
      Consumer<Request> listener = request2 -> System.out.println("Request finished: " + request2.url());
      page.onRequestFinished(listener);
      page.navigate("https://wikipedia.org");
      
      // Remove previously added listener, each on* method has corresponding off*
      page.offRequestFinished(listener);
      page.navigate("https://www.openstreetmap.org/");    
      
     /*  String createTagNameEngine = "{\n" +
  "  // Returns the first element matching given selector in the root's subtree.\n" +
  "  query(root, selector) {\n" +
  "    return root.querySelector(selector);\n" +
  "  },\n" +
  "\n" +
  "  // Returns all elements matching given selector in the root's subtree.\n" +
  "  queryAll(root, selector) {\n" +
  "    return Array.from(root.querySelectorAll(selector));\n" +
  "  }\n" +
  "}";

    // Register the engine. Selectors will be prefixed with "tag=".
    playwright.selectors().register("tag", createTagNameEngine);

    // Now we can use "tag=" selectors.
    Locator button = page.locator("tag=button");
    button.click();

    // We can combine it with built-in locators.
    page.locator("tag=div").getByText("Click me").click();

    // We can use it in any methods supporting selectors.
    int buttonCount = (int) page.locator("tag=button").count();
    }
    */
    /* JSHandle myArrayHandle = page.evaluateHandle("() => {\n" +
      "  window.myArray = [1];\n" +
      "  return myArray;\n" +
      "}");

    // Get the length of the array.
    int length = (int) page.evaluate("a => a.length", myArrayHandle);

    // Add one more element to the array using the handle
    Map<String, Object> arg = new HashMap<>();
    arg.put("myArray", myArrayHandle);
    arg.put("newElement", 2);
    page.evaluate("arg => arg.myArray.add(arg.newElement)", arg);

    // Release the object when it is no longer needed.
    myArrayHandle.dispose();
    */

   // Intercept the route to the fruit API

  
  
    }  
  } 

  public String launchBrowser(String url) {
    System.out.println("This is remind message of sending page title");  
    
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch();
      Page page = browser.newPage();
  
      try {
        page.navigate(url);
        return page.title();
      } catch (PlaywrightException e) {
        return "Navigation failed: " + e.getMessage();
      }
    } catch (PlaywrightException e) {
      return "Error creating or closing browser: " + e.getMessage();
    }
      
  }
 
  public void screenshotWebsite(String url, String filePath) {
    System.out.println("This is remind message of screenshot web-page");
    try (Playwright playwright = Playwright.create()) {
        Browser browser = playwright.webkit().launch();
        Page page = browser.newPage();
        page.navigate(url);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
    } catch (Exception e) {
        
        e.printStackTrace();
        
    }
  }
  
  public String runAutomation() {
    try (Playwright playwright = Playwright.create()) {
        Browser browser = playwright.chromium().launch();
        Page page = browser.newPage();

        page.navigate("http://playwright.dev");
        System.out.println("Navigated to http://playwright.dev");

        assertThat(page).hasTitle(Pattern.compile("Playwright"));
        System.out.println("Page title verified: Playwright");

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
}
  
  
