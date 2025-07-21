package academy.teenfuture.crse.autotestgrasp;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;

//this file is based on: https://www.youtube.com/watch?v=92-qLIxv0JA&t=131s


@Component
public class DemoScheduledTasks {
//fixed rate 
//fixed delay 

//Editor for cron schedule expressions by Cronitor:https://crontab.guru/#5_2_31_2_*
//ps: in spring boot ,cron having 6 string instead of 5

//(cron = "    *         *           *            *            *             *      ")
//         sec(0-59) min(0-59)  hours(0-23)   day(1-31)   month(1-12)   week(SUN-SAT)

//every sec: */s * * * * *
//every min :* */m * * * *

//Important
//every hours :* * */h * * *
//on clock : * * h * *

    @Scheduled(cron = "0 0 10 * * *") //0 0 10 * * *
    void someJob() throws InterruptedException {
        System.out.println("DemoScheduledTasks: Goodmorning , Now time is " + new Date() + ",this is a message sendind on every 10.a.m., have a good day!");
        //Thread.sleep(1000);
    }
}