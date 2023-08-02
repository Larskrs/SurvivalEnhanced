package plugins.larskrs.net.survivalenhanced.tools;

import java.time.*;
import java.time.temporal.TemporalField;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    private static String[] outputStr = new String[]{"year", "month", "week", "day", "hour", "minute","second"};
    private static long[] minisArray = new long[]{
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(7),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1)
    };

    public static String getRelativeTime(final long date) {
        long duration = System.currentTimeMillis() - date;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < minisArray.length - 1; i++) {
            long temp = duration / minisArray[i];
            if (temp > 0) {
                sb.append(temp)
                        .append(" ")
                        .append(outputStr[i])
                        .append(temp > 1 ? "s" : "")
                        .append(" ago");
                break;
            }
        }
        return sb.toString().isEmpty() ? "just now" : sb.toString();
    }
}
