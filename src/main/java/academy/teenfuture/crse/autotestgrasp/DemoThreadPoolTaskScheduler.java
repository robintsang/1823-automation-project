package academy.teenfuture.crse.autotestgrasp;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DemoThreadPoolTaskScheduler {

    private final ThreadPoolTaskScheduler scheduler;

    public DemoThreadPoolTaskScheduler() {
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
    }

    public String schedulerJob() {
        String message = "DemoThreadPoolTaskScheduler: Good morning, Now time is " + new Date() + ", this is a message sending on every 10 seconds, have a good day!";
        System.out.println(message);
        return message;
    }

    public void startScheduler() {
        scheduler.schedule(this::schedulerJob, new CronTrigger("*/10 * * * * *")); // Schedule to run every 10 seconds
    }
}