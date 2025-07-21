package academy.teenfuture.crse.autotestgrasp;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
//This file is based on: https://www.youtube.com/watch?v=LCGr7-ZgiIA

@Component
public class DemoEmailSenderTask {

    @Async
    void sendEmail(List<String> emailList) {
            emailList.forEach(email -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("Sending email to: " + email + " by thread " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
          }
    }
