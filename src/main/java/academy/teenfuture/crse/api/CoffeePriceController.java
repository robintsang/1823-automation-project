package academy.teenfuture.crse.api;
//import academy.teenfuture.crse.service.CoffeePriceService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

import com.microsoft.playwright.*;

@Controller
@RequestMapping("/coffee")
public class CoffeePriceController {
  /*   
  final CoffeePriceService service;

  public CoffeePriceController(CoffeePriceService service) {
    this.service = service;

  }

  @PostMapping("/price/{name}") 
  public ResponseEntity<String> getPriceByCoffeeName(@PathVariable("name") String coffeeName) {
      System.out.println(coffeeName);
      String price = this.service.fetchCoffeePrice(coffeeName);
      System.out.println(price);  
      return ResponseEntity.ok(price);
  }
  
  @PostMapping("/priceSelenium/{name}")
  public ResponseEntity<String> getPriceByCoffeeNameSelenium(@PathVariable("name") String coffeeName) {
      System.out.println(coffeeName);
      String price = this.service.fetchCoffeePriceSelenium(coffeeName);
      System.out.println(price);  
      return ResponseEntity.ok(price);
  }

  @PostMapping("/screenCapture")
  public ResponseEntity<String> screenCapture() {
    this.service.screenCapture();
    return ResponseEntity.ok("screen captured");
  }

  @PostMapping("/screenCaptureSelenium")
  public ResponseEntity<String> screenCapture2() {
    this.service.screenCapture2();
    return ResponseEntity.ok("screen captured");
  }

  @PostMapping("/login")
  public ResponseEntity<String> login() {
    this.service.login();
    return ResponseEntity.ok("logined");
  }

  @PostMapping("/loginSelenium")
  public ResponseEntity<String> login2() {
    this.service.login2();
    return ResponseEntity.ok("logined");
  }

  @PostMapping("/tracing")
  public ResponseEntity<String> tracing() {
    this.service.tracing();
    return ResponseEntity.ok("tracing");
  }

  @PostMapping("/tracing2")
  public ResponseEntity<String> tracing2() {
    this.service.tracing2();
    return ResponseEntity.ok("tracing2");
  }

  @PostMapping("/image")
  public ResponseEntity<String> image() {
    this.service.image();
    return ResponseEntity.ok("image taken");
  }  
  @PostMapping("/imageSelenium")
  public ResponseEntity<String> imageSelenium() {
    this.service.imageSelenium();
    return ResponseEntity.ok("imageSelenium taken");
  } 

  @PostMapping("/searching")
  public ResponseEntity<String> searching() {
    this.service.searching();
    return ResponseEntity.ok("searching");
  }
  */
}

