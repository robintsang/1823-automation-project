package academy.teenfuture.crse.service;

import academy.teenfuture.crse.modal.Remark;
import academy.teenfuture.crse.modal.Transaction;
import academy.teenfuture.crse.repository.RemarkRepository;
import academy.teenfuture.crse.repository.TransactionRepository;
import academy.teenfuture.crse.requestModal.RemarkRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RemarkService {

    @Autowired
    RemarkRepository remarkRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    LogService logService;

    public void createRemark(Long transactionId, RemarkRequest remarkRequest) {
        remarkRepository.save(getRemarkFromRemarkRequest(transactionId, remarkRequest));
    }

    public void createRemark(List<Remark> remarks) {
        List<Remark> saveRemarks = remarks.stream().map(this::RemarkToSaveFormat).collect(Collectors.toList());
        remarkRepository.saveAll(saveRemarks);
    }

    public List<Remark> createRemark(List<Long> transactionIds, RemarkRequest remarkRequest) {
        List<Remark> remarks = transactionIds.stream().map(item -> getRemarkFromRemarkRequest(item, remarkRequest)).collect(Collectors.toList());
        List<Transaction> transactions = remarks.stream().map(Remark::getTransaction).collect(Collectors.toList());
        logService.createLog(transactions, remarkRequest.getUserId(), getLogMessage(remarkRequest.getDescription()));
        return remarkRepository.saveAll(remarks);
    }

    public List<Remark> getRemarksByTransactionId(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        return remarkRepository.findByTransaction(transaction);
    }

    //region Helper
    Remark RemarkToSaveFormat(Remark remark) {
        if (remark.getTransaction() == null)
            throw new IllegalArgumentException("Missing transaction");

        Remark newRemark = new Remark();
        newRemark.setTransaction(remark.getTransaction());
        newRemark.setUserId(remark.getUserId());
        newRemark.setDescription(remark.getDescription());
        return remark;
    }

    Remark getRemarkFromRemarkRequest(Long transactionId, RemarkRequest remarkRequest) {
        Transaction transaction = getTranscationById(transactionId);

        Remark remark = new Remark();
        remark.setTransaction(transaction);
        remark.setUserId(remarkRequest.getUserId());
        remark.setDescription(remarkRequest.getDescription());
        return remark;
    }

    Transaction getTranscationById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction id"));
    }

    String getLogMessage(String description) {
        return "add remark: " + description;
    }
    //endregion
}
