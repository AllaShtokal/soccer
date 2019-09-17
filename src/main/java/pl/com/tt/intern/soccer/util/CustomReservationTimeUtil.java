package pl.com.tt.intern.soccer.util;

import lombok.NoArgsConstructor;
import pl.com.tt.intern.soccer.model.enums.ReservationPeriod;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.LocalDateTime.now;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class CustomReservationTimeUtil {

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

    public static LocalDateTime from(DayOfWeek day) {
        return now().with(previousOrSame(day))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
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

    public static LocalDateTime to(DayOfWeek day) {
        return now().with(nextOrSame(day))
                .withHour(23).withMinute(59).withSecond(59).withNano(0);
    }

}
