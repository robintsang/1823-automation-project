package academy.teenfuture.crse.api;

import academy.teenfuture.crse.modal.DemoUser;
import academy.teenfuture.crse.service.DemoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("demoAuth")
public class DemoAuthController {
    final DemoAuthService service;

    public DemoAuthController(DemoAuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
    try {
        String email = request.get("email");
        String password = request.get("password"); 
        DemoUser user = service.login(email, password); 
        return ResponseEntity.ok(user);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Authorization Failed");
    }
}

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String phoneNumber = request.get("phoneNumber");
            DemoUser user = service.createUser(email, password, phoneNumber);             
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
