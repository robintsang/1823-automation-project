package academy.teenfuture.crse.api;

import academy.teenfuture.crse.service.DemoTFAClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tfaDemo")
public class DemoTFAClassController {
  private final DemoTFAClassService service;

  @Autowired
  public DemoTFAClassController(DemoTFAClassService service) {
    this.service = service;
  }

  @GetMapping("/testFast")
  public ResponseEntity<String> testFast() {
    service.testFast();
    return ResponseEntity.ok("Test execution initiated.");
  }

  @GetMapping("/testSlow")
  public ResponseEntity<String> testSlow() {
    service.testSlow();
    return ResponseEntity.ok("Test execution initiated.");
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
}