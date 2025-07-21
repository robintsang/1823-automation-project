package academy.teenfuture.crse.service.Transaction;

import academy.teenfuture.crse.Enum.LogType;
import academy.teenfuture.crse.Enum.TransactionStatus;
import academy.teenfuture.crse.filter.Filter;
import academy.teenfuture.crse.modal.Log;
import academy.teenfuture.crse.modal.Remark;
import academy.teenfuture.crse.modal.Transaction;
import academy.teenfuture.crse.service.LogService;
import academy.teenfuture.crse.service.RemarkService;
import academy.teenfuture.crse.repository.TransactionRepository;
import academy.teenfuture.crse.requestModal.RemarkRequest;
import academy.teenfuture.crse.requestModal.TransactionRequest;
import academy.teenfuture.crse.requestModal.Transaction_UpdateRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    RemarkService remarkService;
    @Autowired
    LogService logService;
    @Autowired
    ModelMapper modelMapper;

    String[] stayNewRequestItemCode = new String[]{"000000001", "000000003", "000000005", "000000007", "0000000010"};

    Transaction setStatusFromItemCode(Transaction transaction) {
        for (String item : stayNewRequestItemCode) {
            if (Objects.equals(item, transaction.getItemCode()))
                return transaction;
        }
        transaction.setStatus(TransactionStatus.PendingApproval);
        transaction.setNewRequest(false);
        return transaction;
    }

    public Transaction createTransaction(TransactionRequest transactionRequest) {
        Transaction transactionFromRequest = setStatusFromItemCode(RequestToTransaction(transactionRequest));
        Transaction transaction = transactionRepository.save(transactionFromRequest);
        logService.createLog(transaction, transactionRequest.getUserId(), LogType.createTransaction);
        if (!transactionRequest.getRemark().isEmpty())
            remarkService.createRemark(transaction.getId(), getRemarkRequestByString(transactionRequest.getUserId(), transactionRequest.getRemark()));
        return transaction;
    }

    @Transactional
    public List<Transaction> createTransaction(List<TransactionRequest> transactionRequests) {
        List<Transaction> transactions = new ArrayList<>();
        HashMap<Integer, Remark> remarkMap = new HashMap<>();

        for (int i = 0; i < transactionRequests.size(); i++) {
            TransactionRequest item = transactionRequests.get(i);
            transactions.add(setStatusFromItemCode(RequestToTransaction(item)));

            if (!item.getRemark().isEmpty()) {
                remarkMap.put(i, getRemarkByString(item.getUserId(), item.getRemark()));
            }
        }
        List<Transaction> savedTransactions = transactionRepository.saveAll(transactions);
        logService.createLog(transactions, transactionRequests.get(0).getUserId(), LogType.createTransaction);

        List<Remark> remarks = remarkMap.keySet().stream().map((key) -> {
            Remark remark = remarkMap.get(key);
            remark.setTransaction(savedTransactions.get(key));
            return remark;
        }).collect(Collectors.toList());

        remarkService.createRemark(remarks);
        return savedTransactions;
    }

    public Slice<Transaction> getTransaction(String search, int page, int size, String sort, String sortMode, Map<String, String> params) {
        Specification<Transaction> spec = Filter.getFilterSearch(Transaction.class, params, search);
        return Filter.getSlicePage(transactionRepository, spec, sort, sortMode, page, size);
    }

    public List<Transaction> updateTransaction(List<Long> ids, Transaction_UpdateRequest request) {
        List<Transaction> transactions = ids.stream().map((Long id) -> getUpdatedTransaction(id, request)).collect(Collectors.toList());

        List<Log> logs = IntStream.range(0, ids.size()).mapToObj(i -> {
            String message = getUpdateLogMessage(getTransactionById(ids.get(i)), transactions.get(i));
            if (message.isEmpty()) return null;
            return logService.getLogByData(transactions.get(i), request.getUserId(), message);
        }).collect(Collectors.toList());

        logService.createLog(logs);
        transactionRepository.saveAll(transactions);

        if (request.getRemark().isEmpty()) return transactions;

        RemarkRequest remarkRequest = getRemarkRequestByString(request.getUserId(), request.getRemark());
        remarkService.createRemark(ids, remarkRequest);
        return transactions;
    }

    public Transaction getUpdatedTransaction(Long id, Transaction_UpdateRequest transactionUpdateRequest) {
        Transaction transaction = getTransactionById(id);
        Transaction updatedTransaction = new Transaction();
        BeanUtils.copyProperties(transaction, updatedTransaction);
        try {
            modelMapper.map(transactionUpdateRequest, updatedTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Error updating transaction", e);
        }
        return updatedTransaction;
    }


    //region Initialize
    public void createFakeData(int numEntities) {
        System.out.println("creating Fake Transaction Data");
        List<Transaction> transactions = Stream.generate(() -> {
            Transaction entity = new Transaction();
            TransactionStatus randomEnum = TransactionStatus.random();
            entity.setStatus(randomEnum);
            return entity;
        }).limit(numEntities).collect(Collectors.toList());

        transactionRepository.saveAll(transactions);
    }
    //endregion

    //region Helper
    String getUpdateLogMessage(Transaction old, Transaction updated) {
        StringBuilder messageBuilder = new StringBuilder();
        Field[] fields = Transaction.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            addLogMessage(field, old, updated, messageBuilder);
        }
        return messageBuilder.toString();
    }

    void addLogMessage(Field field, Transaction old, Transaction updated, StringBuilder builder) {
        String key = field.getName();
        Object oldValue = null;
        Object newValue = null;
        try {
            oldValue = field.get(old);
            newValue = field.get(updated);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (Objects.equals(oldValue, newValue))
            return;

        switch (key) {
            case "status":
                builder.append("change request status to " + newValue);
                break;
            default:
                builder.append("edit ").append(key).append("from ").append(oldValue).append(" to ").append(newValue);
        }
        builder.append(System.lineSeparator());
    }

    Log getLogByString(Long userId, String text, Transaction transaction) {
        Log log = new Log();
        log.setUserId(userId);
        log.setDescription(text);
        log.setTransaction(transaction);
        return log;
    }

    Remark getRemarkByString(Long userId, String text, Transaction transaction) {
        Remark remark = new Remark();
        remark.setUserId(userId);
        remark.setDescription(text);
        remark.setTransaction(transaction);
        return remark;
    }

    Remark getRemarkByString(Long userId, String text) {
        Remark remark = new Remark();
        remark.setUserId(userId);
        remark.setDescription(text);
        return remark;
    }

    RemarkRequest getRemarkRequestByString(Long userId, String remark) {
        RemarkRequest remarkRequest = new RemarkRequest();
        remarkRequest.setUserId(userId);
        remarkRequest.setDescription(remark);
        return remarkRequest;
    }

    Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid transaction id " + +id));
    }

    public Transaction RequestToTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setLocation(transactionRequest.getLocation());
        transaction.setItemCode(transactionRequest.getItemCode());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setWarehouse(transactionRequest.getWarehouse());
        transaction.setUserRequestQty(transactionRequest.getUserRequestQty());
        transaction.setProjectCode(transactionRequest.getProjectCode());
        transaction.setOrderId(transactionRequest.getOrderId());
        transaction.setSku(transactionRequest.getSku());
        return transaction;
    }
    //endregion
}
