package academy.teenfuture.crse.api;

import academy.teenfuture.crse.modal.Transaction;
import academy.teenfuture.crse.requestModal.TransactionRequest;
import academy.teenfuture.crse.requestModal.Transaction_UpdateRequest;
import academy.teenfuture.crse.service.Transaction.TransactionService;
import academy.teenfuture.crse.service.Transaction.TransactionUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("japi/wung/quasar2/transaction")
@RestController
public class TransactionController {
    final TransactionService transactionService;
    final TransactionUpdateService transactionUpdateService;

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionUpdateService transactionUpdateService) {
        this.transactionService = transactionService;
        this.transactionUpdateService = transactionUpdateService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.createTransaction(transactionRequest);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Transaction>> createTransaction(@RequestBody List<TransactionRequest> transactionRequests) {
        List<Transaction> transactions = transactionService.createTransaction(transactionRequests);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping()
    public ResponseEntity<?> getTransaction(@RequestParam(required = false) String search,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "-1") int pageSize,
                                            @RequestParam(defaultValue = "id") String sort,
                                            @RequestParam(defaultValue = "ASC") String sortMode,
                                            @RequestParam(required = false) Map<String, String> params) {
        try {
            Slice<Transaction> transactions = transactionService.getTransaction(search, page, pageSize, sort, sortMode, params);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //region Update
    @PutMapping("/{transactionIds}")
    public ResponseEntity<?> updateTransaction(@PathVariable List<Long> transactionIds, @RequestBody Transaction_UpdateRequest transactionUpdateRequest) {
        try {
            List<Transaction> transactions = transactionService.updateTransaction(transactionIds, transactionUpdateRequest);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/cancel/{transactionIds}")
    public ResponseEntity<?> cancelTransaction(@PathVariable List<Long> transactionIds, @RequestBody Map<String, Object> requestBody) {
        try {
            String remark = (String) requestBody.get("remark");
            Long userId = ((Integer) requestBody.get("userId")).longValue();
            List<Transaction> transactions = transactionUpdateService.cancelTransaction(transactionIds, userId, remark);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/returnAll/{transactionIds}")
    public ResponseEntity<?> returnAllTransaction(@PathVariable List<Long> transactionIds, @RequestBody Map<String, Object> requestBody) {
        try {
            Long userId = ((Integer) requestBody.get("userId")).longValue();
            String remark = (String) requestBody.get("remark");
            boolean returnAll = (Boolean) requestBody.get("returnAll");
            boolean salesReturnAll = (Boolean) requestBody.get("salesReturnAll");
            List<Transaction> transactions = transactionUpdateService.returnAllTransaction(transactionIds, userId, remark, returnAll, salesReturnAll);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/return/{transactionId}")
    public ResponseEntity<?> returnTransaction(@PathVariable Long transactionId, @RequestBody Map<String, Object> requestBody) {
        try {
            String returnType = (String) requestBody.get("type");
            int qty = (int) requestBody.get("qty");
            Long userId = ((Integer) requestBody.get("userId")).longValue();
            String remark = (String) requestBody.get("remark");
            Transaction transactions = transactionUpdateService.returnTransaction(transactionId, userId, remark, qty, returnType);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/approve/{transactionIds}")
    public ResponseEntity<?> approveTransaction(@PathVariable List<Long> transactionIds, @RequestBody Map<String, Object> requestBody) {
        try {
            String type = (String) requestBody.get("type");
            Long userId = ((Integer) requestBody.get("userId")).longValue();
            String remark = (String) requestBody.get("remark");
            List<Transaction> transactions = transactionUpdateService.approveTransaction(transactionIds, userId, remark, type);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/close/{transactionIds}")
    public ResponseEntity<?> closeTransaction(@PathVariable List<Long> transactionIds, @RequestBody Map<String, Object> requestBody) {
        try {
            Long userId = ((Integer) requestBody.get("userId")).longValue();
            String remark = (String) requestBody.get("remark");
            List<Transaction> transactions = transactionUpdateService.closeTransaction(transactionIds, userId, remark);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/verify/{transactionIds}")
    public ResponseEntity<?> verifyTransaction(@PathVariable List<Long> transactionIds, @RequestBody Map<String, Object> requestBody) {
        try {
            Long userId = ((Integer) requestBody.get("userId")).longValue();
            String remark = (String) requestBody.get("remark");
            List<Transaction> transactions = transactionUpdateService.verifyTransaction(transactionIds, userId, remark);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/flagPicking/{transactionIds}")
    public ResponseEntity<?> flagPickingTransaction(@PathVariable List<Long> transactionIds, @RequestBody Map<String, Object> requestBody) {
        try {
            Long userId = ((Integer) requestBody.get("userId")).longValue();
            List<Transaction> transactions = transactionUpdateService.flagPickingTransaction(transactionIds, userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/unFlagPicking/{transactionIds}")
    public ResponseEntity<?> unFlagPickingTransaction(@PathVariable List<Long> transactionIds, @RequestBody Map<String, Object> requestBody) {
        try {
            Long userId = ((Integer) requestBody.get("userId")).longValue();
            List<Transaction> transactions = transactionUpdateService.unFlagPickingTransaction(transactionIds, userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/deliver/{transactionIds}")
    public ResponseEntity<?> deliverTransaction(@PathVariable List<Long> transactionIds, @RequestBody Map<String, Object> requestBody) {
        try {
            String type = (String) requestBody.get("type");
            Long userId = ((Integer) requestBody.get("userId")).longValue();
            String remark = (String) requestBody.get("remark");
            List<Transaction> transactions = transactionUpdateService.deliverTransaction(transactionIds, userId, remark, type);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //endregion
}


