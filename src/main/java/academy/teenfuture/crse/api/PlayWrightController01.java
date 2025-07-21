package academy.teenfuture.crse.api;

import academy.teenfuture.crse.service.PlayWrightService01;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import com.microsoft.playwright.Route.FulfillOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import java.util.function.Consumer;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import academy.teenfuture.crse.service.*;

@Controller
@RequestMapping("/web-scrap")
public class PlayWrightController01 {
  private final PlayWrightService01 service;

  public PlayWrightController01(PlayWrightService01 service) {
    this.service = service;
  }

  /*
   * 
  */
  /* 
  @PostMapping("/call")
  public void scrap() {
    service.callTest();
  }

  @PostMapping("/navigate")
  public ResponseEntity<String> navigateToUrl() {
    String pageTitle = service.launchBrowser("https://hk.yahoo.com/");
    return ResponseEntity.ok(pageTitle);
  }

  @PostMapping("/screenshot")
  public ResponseEntity<String> screenshotWebsite() {
    String url = "https://playwright.dev/";
    String filePath = "example.png";
    service.screenshotWebsite(url, filePath);
    return ResponseEntity.ok("Website screenshot captured.");
  }

  @PostMapping("/run-automation")
  public ResponseEntity<String> runAutomation() {
    try {
      String result = service.runAutomation();
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
    }
  }

  @PostMapping("/test")
  public ResponseEntity<String> runTest() {
    service.callTest();
    return ResponseEntity.ok("hi");    
  }
 */

}

