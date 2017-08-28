package gaf.util;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class CortesExpiredChecker {

    @Schedule(second = "*", minute = "*", hour = "*/24", persistent = false)
    public void doWork() {
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        System.out.println("CortesExpiredChecker.doWork() invoked at " + simpleDateFormat.format(currentTime));
    }

}
