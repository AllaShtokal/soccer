package pl.com.tt.intern.soccer.util;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class DateUtil {

    private static final int TIME_ZONE_2_HOURS_CORRECTION_IN_SEC = 7200;

    public static Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant().minusSeconds(TIME_ZONE_2_HOURS_CORRECTION_IN_SEC));
    }
}
