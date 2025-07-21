package academy.teenfuture.crse.service;

import academy.teenfuture.crse.Enum.LogType;
import academy.teenfuture.crse.modal.Log;
import academy.teenfuture.crse.modal.Transaction;
import academy.teenfuture.crse.repository.LogRepository;
import academy.teenfuture.crse.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {
    @Autowired
    LogRepository logRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public void createLog(Long transactionId, Long userId, LogType type) {
        createLog(getTransaction(transactionId), userId, type);
    }

    public void createLog(Transaction transaction, Long userId, LogType type) {
        logRepository.save(getLogByData(transaction, userId, type));
    }

    public void createLog(List<Transaction> transactions, Long userId, LogType type) {
        List<Log> logs = transactions.stream().map((item) -> getLogByData(item, userId, type)).collect(Collectors.toList());
        logRepository.saveAll(logs);
    }

    public void createLog(List<Log> logs) {
        logRepository.saveAll(logs);
    }

    public void createLog(List<Transaction> transactions, Long userId, String description) {
        List<Log> logs = transactions.stream().map((item) -> getLogByData(item, userId, description)).collect(Collectors.toList());
        logRepository.saveAll(logs);
    }

    Log getLogByData(Transaction transaction, Long userId, LogType type) {
        return getLogByData(transaction, userId, getDescriptionByType(type));
    }

    public List<Log> getLogByTransactionId(Long transacitonId){
        Transaction transaction = getTransaction(transacitonId);
        return logRepository.findByTransaction(transaction);
    }

    public Log getLogByData(Transaction transaction, Long userId, String description) {
        Log log = new Log();
        log.setUserId(userId);
        log.setTransaction(transaction);
        log.setDescription(description);
        return log;
    }

    String getDescriptionByType(LogType type) {
        switch (type) {
            case createTransaction:
                return "Create Transaction";
            case updateTransaction:
                return "Update Transaction";
        }
        return null;
    }

    Transaction getTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction id"));
    }
}
