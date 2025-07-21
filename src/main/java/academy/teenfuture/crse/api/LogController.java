package academy.teenfuture.crse.api;

import academy.teenfuture.crse.modal.Log;
import academy.teenfuture.crse.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("japi/wung/quasar2/log")
@RestController
public class LogController {
    final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getLogByTransactionId(@PathVariable Long transactionId) {
        try {
            List<Log> log = logService.getLogByTransactionId(transactionId);
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}