package academy.teenfuture.crse.repository;

import academy.teenfuture.crse.modal.Log;
import academy.teenfuture.crse.modal.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByTransaction(Transaction transaction);
}
