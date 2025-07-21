package academy.teenfuture.crse.utility;

import academy.teenfuture.crse.service.AuthService;
import academy.teenfuture.crse.service.Transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FakeDataInitializer {
  @Autowired
  TransactionService transactionService;

  @Autowired
  AuthService authService;

  @PostConstruct
  public void init() {
    System.out.println("Init Start");
    transactionService.createFakeData(0);
    authService.createFakeData();
    System.out.println("Init Finish");
  }
}
