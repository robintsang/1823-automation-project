package academy.teenfuture.crse.api;

import academy.teenfuture.crse.modal.User;
import academy.teenfuture.crse.service.AuthService;
import academy.teenfuture.crse.requestModal.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("japi/wung/quasar2/auth")
public class AuthController {
    final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            User user = authService.login(request.get("token"));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        try {
            User user = authService.createUser(request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String search,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "-1") int pageSize,
                                      @RequestParam(defaultValue = "id") String sort,
                                      @RequestParam(defaultValue = "ASC") String sortMode,
                                      @RequestParam(required = false) Map<String, String> params) {
        try {
            Slice<User> users = authService.getUsers(search, sort, sortMode, page, pageSize, params);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId,@RequestBody UserRequest request) {
        try {
            User user = authService.editUser(userId, request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
