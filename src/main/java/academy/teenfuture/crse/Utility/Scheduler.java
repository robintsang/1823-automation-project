package academy.teenfuture.crse.utility;

import academy.teenfuture.crse.service.Transaction.TransactionUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    @Autowired
    TransactionUpdateService transactionUpdateService;

    @Scheduled(cron = "0 33 10 * * *")
    // code to be executed at 00:00 every day
    public void Task() {
        System.out.println("Start Schedule Task");
        transactionUpdateService.completeAllDelivered((long) -1);
        System.out.println("Finish Schedule Task");
    }
}
