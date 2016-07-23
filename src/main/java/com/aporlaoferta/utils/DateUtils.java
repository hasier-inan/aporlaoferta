package com.aporlaoferta.utils;

import com.aporlaoferta.model.DateRange;

import java.util.Calendar;
import java.util.Date;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 23/07/16
 * Time: 17:35
 */
public class DateUtils {

    public static DateRange getDateRange(int position) {
        switch (position) {
            case 0:
                return DateRange.DAY;
            case 2:
                return DateRange.MONTH;
            case 3:
                return DateRange.ALL;
            default:
                return DateRange.WEEK;
        }
    }

    public static int parseDateRangeDays(DateRange dateRange) {
        return !isEmpty(dateRange) ? dateRange.getDays() : DateRange.WEEK.getDays();
    }

    public static Date parseDateRangeOnDate(DateRange dateRange) {
        return parseFromActualDate(parseDateRangeDays(dateRange));
    }

    public static Date parseFromActualDate(int days) {
        if (days == 0) {
            return new Date(0);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }
}
