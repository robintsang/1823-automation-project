package academy.teenfuture.crse.autotestgrasp;

import academy.teenfuture.crse.autotestgrasp.AutoTestService;
import academy.teenfuture.crse.autotestgrasp.DemoEmailSenderTask;

import com.microsoft.playwright.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.ArrayList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/web-scrap")
public class AutoTestController {
    private final AutoTestService service;

    @Autowired
    public AutoTestController(AutoTestService service) {
        this.service = service;
    }
    @Autowired
    private DemoEmailSenderTask asyncTask;


    //Use async to process functions together, regardless of whether the previous function has completed
    @GetMapping({"/", "/{count}"})
    public ResponseEntity<String> testAsync(@PathVariable("count") Optional <Integer> count) {
      System.out.println("Start testasync - " + Thread.currentThread().getName());
      asyncTask.sendEmail(prepareEmailList(count.isPresent() ? count.get() : 1));
      System.out.println("End testasync - " + Thread.currentThread().getName());
      return ResponseEntity.ok("Email sending process starts");
    }

    private List<String> prepareEmailList(int n){
      List<String> emails = new ArrayList<>();
      for(int i = 0; i < n;i++){
        emails.add("test"+ i +"@gmail.com");
        
      };
      return emails;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
      return ResponseEntity.ok(service.getHelloMessage());
    }

    @PostMapping("/call")
    public void scrap() {
        service.callTest();  
    }
    
    @DeleteMapping("/delete/file")
    @ResponseBody
      public String deleteFiles() {
          service.deleteFilesInPath();
          return "All files in videos path deleted";
      }
      
    @DeleteMapping("/delete/all")
    @ResponseBody
      public String deleteAll() {
          service.deleteAll();
          return "All files in videos path deleted";
      }
      
    @PostMapping("/runningtest")
    public ResponseEntity<String> runningTests() {
       return ResponseEntity.ok(service.runningTests());
    }

    @PostMapping("/playw-navigate")
    public ResponseEntity<String> launchBrowser() {
        return ResponseEntity.ok(service.launchBrowser());
    }

    

    @PostMapping("/runAutomation")
    public ResponseEntity<String> runAutomation() {
        try {
            return ResponseEntity.ok(service.runAutomation());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/RRUrl")
    public ResponseEntity<String> requestResponse() {
        try {
            service.requestResponse();
            return ResponseEntity.ok("URL executed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/auto-login")
    public ResponseEntity<String> loginBrowser() {
        return ResponseEntity.ok(service.loginBrowser());
    }

    @PostMapping("/auto-g-login")
    public ResponseEntity<String> loginGoogle() {
        return ResponseEntity.ok(service.loginGoogle());
    }

    @PostMapping("/tracing")
    public ResponseEntity<String> tracingBrowser() {
        return ResponseEntity.ok(service.tracingBrowser());
    }

        
    @PostMapping("/sele-navigate")
    public ResponseEntity<String> performAutomatedTest() {
        return ResponseEntity.ok(service.performAutomatedTest());
    }
    
    @PostMapping("/screenshot-word")
    public ResponseEntity<String> takeScreenshot(@RequestBody String requestBody) {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        String library = jsonNode.get("library").asText();

        if ("playwright".equals(library)) {
            service.screenshotPlaywirght();
            return ResponseEntity.ok("Website screenshot captured using Playwright.");
        } else if ("selenium".equals(library)) {
          service.screenshotSelenium();          
            return ResponseEntity.ok("Website screenshot captured using Selenium");
        } else {
            return ResponseEntity.badRequest().body("Invalid library specified.");
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error capturing website screenshot: " + e.getMessage());
    }
}
  @GetMapping("/screenshot-img")
  public ResponseEntity<byte[]> getScreenshot() {
    try {
        service.screenshotSeleniumImg(); // 
        byte[] screenshotData = service.getImgData();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_PNG);
      headers.setContentLength(screenshotData.length);

      return new ResponseEntity<>(screenshotData, headers, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
/* code haven't finish yet
  @PostMapping("/screenshot-return-img")
  public ResponseEntity<byte[]> takeScreenshotAndUpload(@RequestBody Map<String, String> requestBody) {
      try {
          String library = requestBody.get("library");

          if ("playwright".equals(library)) {
            byte[] screenshotBytes = autoTestService.captureScreenshotSelenium();
            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .body(screenshotBytes);
        }
          } else if ("selenium".equals(library)) {
            byte[] screenshotBytes = autoTestService.captureScreenshotPlaywright();
            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .body(screenshotBytes);
          } else {
              return ResponseEntity.badRequest().body("Invalid library specified.");
          }
      } catch (Exception e) {
          return ResponseEntity.badRequest().body("Error capturing website screenshot: " + e.getMessage());
      }
    }
  */


}
