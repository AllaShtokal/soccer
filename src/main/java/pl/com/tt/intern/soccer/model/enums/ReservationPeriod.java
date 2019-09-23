package pl.com.tt.intern.soccer.model.enums;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.LocalDateTime.now;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

public enum ReservationPeriod {
    TODAY(
            () -> now().withHour(0).withMinute(0).withSecond(0).withNano(0),
            () -> now().withHour(23).withMinute(59).withSecond(59).withNano(0)
    ),
    WEEK(
            () -> now().with(previousOrSame(MONDAY)).withHour(0).withMinute(0).withSecond(0).withNano(0),
            () -> now().with(nextOrSame(SUNDAY)).withHour(23).withMinute(59).withSecond(59).withNano(0)

    ),
    MONTH(
            () -> now().withDayOfMonth(1)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0),
            () -> now().withDayOfMonth(now().toLocalDate().lengthOfMonth())
                    .withHour(23).withMinute(59).withSecond(59).withNano(0)

    );

    private final Supplier<LocalDateTime> from;
    private final Supplier<LocalDateTime> to;

    ReservationPeriod(Supplier<LocalDateTime> from, Supplier<LocalDateTime> to) {
        this.from = from;
        this.to = to;
    }

    public final LocalDateTime from() {
        return this.from.get();
    }

    public final LocalDateTime to() {
        return this.to.get();
    }

}
