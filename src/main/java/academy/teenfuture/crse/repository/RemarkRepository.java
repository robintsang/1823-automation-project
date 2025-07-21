package academy.teenfuture.crse.repository;

import academy.teenfuture.crse.modal.Remark;
import academy.teenfuture.crse.modal.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RemarkRepository extends JpaRepository<Remark, Long> {
    List<Remark> findByTransaction(Transaction transaction);
}