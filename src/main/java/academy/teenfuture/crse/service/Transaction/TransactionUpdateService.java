package academy.teenfuture.crse.service.Transaction;

import academy.teenfuture.crse.Enum.TransactionStatus;
import academy.teenfuture.crse.modal.Log;
import academy.teenfuture.crse.modal.Remark;
import academy.teenfuture.crse.modal.Transaction;
import academy.teenfuture.crse.service.LogService;
import academy.teenfuture.crse.service.RemarkService;
import academy.teenfuture.crse.repository.TransactionRepository;
import com.mysql.cj.exceptions.NumberOutOfRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionUpdateService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    RemarkService remarkService;
    @Autowired
    LogService logService;

    TransactionStatus[] cancelValid = new TransactionStatus[]{TransactionStatus.NewRequest, TransactionStatus.PendingApproval, TransactionStatus.Approved, TransactionStatus.Delivered, TransactionStatus.Completed};
    TransactionStatus[] returnValid = new TransactionStatus[]{TransactionStatus.Delivered, TransactionStatus.Completed};
    TransactionStatus[] approveValid = new TransactionStatus[]{TransactionStatus.PendingApproval};
    TransactionStatus[] closedValid = new TransactionStatus[]{TransactionStatus.Delivered, TransactionStatus.Completed};
    TransactionStatus[] flagPickingValid = new TransactionStatus[]{TransactionStatus.Approved, TransactionStatus.NewRequest};
    TransactionStatus[] unFlagPickingValid = new TransactionStatus[]{TransactionStatus.Picking};
    TransactionStatus[] deliverValid = new TransactionStatus[]{TransactionStatus.Picking};

    public List<Transaction> cancelTransaction(List<Long> transactionIds, Long userId, String remark) {
        return updateTransaction(transactionIds, userId, remark, cancelValid, TransactionStatus.Cancel, item -> "change request status to Cancel");
    }

    public List<Transaction> approveTransaction(List<Long> transactionIds, Long userId, String remark, String type) throws InvalidPropertiesFormatException {
        if (!Objects.equals(type, "Approved") && !Objects.equals(type, "Rejected"))
            throw new InvalidPropertiesFormatException("type of " + type + " is not valid");

        return updateTransaction(transactionIds, userId, remark, approveValid, TransactionStatus.valueOf(type), item -> "change request status to " + TransactionStatus.valueOf(type));
    }

    public List<Transaction> flagPickingTransaction(List<Long> transactionIds, Long userId) {
        flagPickingVerify(); //TODO:
        return updateTransaction(transactionIds, userId, "", flagPickingValid, TransactionStatus.Picking, item -> "flag picking");
    }

    public List<Transaction> unFlagPickingTransaction(List<Long> transactionIds, Long userId) {
        unFlagPickingVerify(); //TODO:
        return updateTransaction(transactionIds, userId, "", unFlagPickingValid, TransactionStatus.Picking,
                item -> "un-flag picking",
                item -> {
                    TransactionStatus to = TransactionStatus.NewRequest;
                    if (!item.isNewRequest())
                        to = TransactionStatus.Approved;
                    item.setStatus(to);
                });
    }

    public List<Transaction> deliverTransaction(List<Long> transactionIds, Long userId, String remark, String type) throws InvalidPropertiesFormatException {
        if (!Objects.equals(type, "Delivered") && !Objects.equals(type, "Rejected"))
            throw new InvalidPropertiesFormatException("type of " + type + " is not valid");

        return updateTransaction(transactionIds, userId, remark, deliverValid, TransactionStatus.valueOf(type),
                item -> "change request status to " + TransactionStatus.valueOf(type),
                item -> {
                    item.setDeliveredQty(item.getUserRequestQty());
                    item.setUserRequestQty(0);
                    item.setStatus(TransactionStatus.valueOf(type));
                });
    }

    public List<Transaction> closeTransaction(List<Long> transactionIds, Long userId, String remark) {
        return updateTransaction(transactionIds, userId, remark, closedValid, TransactionStatus.Closed,
                item -> "change request status to Closed",
                item -> {
                    item.setStatus(TransactionStatus.Closed);
                    item.setUserNoReturnQty(item.getBalanceQty());
                });
    }

    public List<Transaction> verifyTransaction(List<Long> transactionIds, Long userId, String remark) {
        return updateTransaction(transactionIds, userId, remark, closedValid, TransactionStatus.Completed,
                item -> "verify Return Request qty " + (item.getUserReturnedQty() + item.getUserSalesReturnedQty()),
                item -> {
                    item.setUserNoReturnQty(item.getUserNoReturnQty() - item.getUserSalesReturnedQty());
                    item.setUserReturnedQty(item.getUserReturnedQty() + item.getUserSalesReturnedQty());
                    item.setUserSalesReturnedQty(0);
                    TransactionStatus to = TransactionStatus.Closed;
                    if (item.getUserNoReturnQty() != 0)
                        to = TransactionStatus.Completed;
                    item.setStatus(to);
                });
    }

    public List<Transaction> returnAllTransaction(List<Long> transactionIds, Long userId, String remark, boolean returnAll, boolean salesReturnAll) {
        List<Transaction> transactions = new ArrayList<>();

        if (returnAll) {
            transactions.addAll(updateTransaction(transactionIds, userId, remark, returnValid, TransactionStatus.Closed,
                    item -> "submit Return Request qty " + item.getBalanceQty(),
                    item -> {
                        if (item.getStatus() != TransactionStatus.Delivered)
                            return;
                        item.setUserReturnRequestQty(item.getBalanceQty());
                        item.setStatus(TransactionStatus.Closed);
                    }));
        }

        if (salesReturnAll) {
            transactions.addAll(updateTransaction(transactionIds, userId, remark, returnValid, TransactionStatus.Closed,
                    item -> "submit Sales Return Request qty " + item.getBalanceQty(),
                    item -> {
                        if (item.getStatus() != TransactionStatus.Completed)
                            return;
                        item.setUserReturnRequestQty(item.getUserNoReturnQty());
                        item.setStatus(TransactionStatus.Closed);
                    }));
        }

        return transactionRepository.saveAll(transactions);
    }

    public Transaction returnTransaction(Long transactionId, Long userId, String remark, int returnQty, String type) throws InvalidPropertiesFormatException {
        List<Long> transactionIds = new ArrayList<>();
        List<Transaction> transactions;
        transactionIds.add(transactionId);

        switch (type) {
            case "Return" -> {
                transactions = updateTransaction(transactionIds, userId, remark, returnValid, TransactionStatus.Completed,
                        item -> "submit Return Request qty " + returnQty,
                        item -> {
                            if (returnQty > item.getBalanceQty())
                                throw new NumberOutOfRange("returnQty is not valid");

                            item.setUserReturnRequestQty(returnQty);
                            TransactionStatus to = TransactionStatus.Completed;
                            if (returnQty == item.getBalanceQty())
                                to = TransactionStatus.Closed;
                            item.setStatus(to);
                        });
            }
            case "SalesReturn" -> {
                transactions = updateTransaction(transactionIds, userId, remark, returnValid, TransactionStatus.Completed,
                        item -> "submit Sales Return Request qty " + returnQty,
                        item -> {
                            if (returnQty > item.getUserNoReturnQty())
                                throw new NumberOutOfRange("returnQty is not valid");

                            item.setUserSalesReturnedQty(returnQty);
                            TransactionStatus to = TransactionStatus.Completed;
                            if (returnQty == item.getUserNoReturnQty())
                                to = TransactionStatus.Closed;
                            item.setStatus(to);
                        });
            }
            default -> throw new InvalidPropertiesFormatException("type of " + type + " is not valid");
        }

        return transactions.get(0);
    }

    //region Helper
    void flagPickingVerify() {
    }

    void unFlagPickingVerify() {
    }

    List<Transaction> updateTransaction(List<Long> transactionIds, Long userId, String remark, TransactionStatus[] validFrom, TransactionStatus to, LogCallback getLogMsg) {
        return updateTransaction(transactionIds, userId, remark, validFrom, to, getLogMsg, item -> {
            item.setStatus(to);
        });
    }

    List<Transaction> updateTransaction(List<Long> transactionIds, Long userId, String remark, TransactionStatus[] validFrom, TransactionStatus to, LogCallback getLogMsg, Callback onUpdate) {
        List<Transaction> transactions = transactionRepository.findAllById(transactionIds);
        List<Log> logs = new ArrayList<>();
        List<Remark> remarks = new ArrayList<>();

        for (Transaction item : transactions) {
            if (isStatusValid(item.getStatus(), validFrom))
                throw new IllegalStateException("State " + item.getStatus() + " Cannot Be Update As " + to);

            if (!remark.isEmpty())
                remarks.add(transactionService.getRemarkByString(userId, remark, item));
            logs.add(transactionService.getLogByString(userId, getLogMsg.call(item), item));
            onUpdate.call(item);
        }

        List<Transaction> updatedTransactions = transactionRepository.saveAll(transactions);
        logService.createLog(logs);
        remarkService.createRemark(remarks);
        return updatedTransactions;
    }

    public List<Transaction> completeAllDelivered(Long userId) {
        List<Transaction> transactions = transactionRepository.findAll();
        List<Log> logs = new ArrayList<>();
        TransactionStatus[] valid = new TransactionStatus[]{TransactionStatus.Delivered};

        for (Transaction item : transactions) {
            if (isStatusValid(item.getStatus(), valid))
                return transactions;
            logs.add(transactionService.getLogByString(userId, "Auto Complete From Delivered", item));
            item.setStatus(TransactionStatus.Completed);
            item.setUserNoReturnQty(item.getBalanceQty());
        }

        List<Transaction> updatedTransactions = transactionRepository.saveAll(transactions);
        logService.createLog(logs);
        return updatedTransactions;
    }

    interface Callback {
        void call(Transaction a);
    }

    interface LogCallback {
        String call(Transaction a);
    }

    Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid transaction id " + +id));
    }

    boolean isStatusValid(TransactionStatus status, TransactionStatus[] valid) {
        for (TransactionStatus item : valid) {
            if (status == item)
                return false;
        }
        return true;
    }
    //endregion
}
