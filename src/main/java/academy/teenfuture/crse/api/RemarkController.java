package academy.teenfuture.crse.api;

import academy.teenfuture.crse.modal.Remark;
import academy.teenfuture.crse.service.RemarkService;
import academy.teenfuture.crse.requestModal.RemarkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("japi/wung/quasar2/remark")
@RestController
public class RemarkController {
    final RemarkService remarkService;

    @Autowired
    public RemarkController(RemarkService remarkService) {
        this.remarkService = remarkService;
    }

    @PostMapping("/{transactionIds}")
    public ResponseEntity<List<Remark>> createRemark(@PathVariable List<Long> transactionIds, @RequestBody RemarkRequest remarkRequest) {
        List<Remark> remarks = remarkService.createRemark(transactionIds, remarkRequest);
        return ResponseEntity.ok(remarks);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<List<Remark>> getRemarksByTransactionId(@PathVariable Long transactionId) {
        List<Remark> remark = remarkService.getRemarksByTransactionId(transactionId);
        return ResponseEntity.ok(remark);
    }
}