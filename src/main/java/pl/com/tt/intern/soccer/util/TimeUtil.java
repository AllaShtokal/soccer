package pl.com.tt.intern.soccer.util;

import pl.com.tt.intern.soccer.model.enums.ReservationPeriod;

import java.time.LocalDateTime;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.LocalDateTime.now;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

public final class TimeUtil {

    public static LocalDateTime from(ReservationPeriod period) {
        switch (period) {
            case WEEK:
                return now().with(previousOrSame(MONDAY))
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case MONTH:
                return now().withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            default:
                return now()
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
    }

    public static LocalDateTime to(ReservationPeriod period) {
        switch (period) {
            case WEEK:
                return now().with(nextOrSame(SUNDAY))
                        .withHour(23).withMinute(59).withSecond(59).withNano(0);
            case MONTH:
                return now().withDayOfMonth(now().toLocalDate().lengthOfMonth())
                        .withHour(23).withMinute(59).withSecond(59).withNano(0);
            default:
                return now()
                        .withHour(23).withMinute(59).withSecond(59).withNano(0);
        }
    }

}
