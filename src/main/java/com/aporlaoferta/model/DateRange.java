package com.aporlaoferta.model;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 23/07/16
 * Time: 13:10
 */
public enum DateRange {
    DAY(0, 1),
    WEEK(1, 7),
    MONTH(2, 31),
    ALL(3, 0);

    private int code;
    private int days;

    private DateRange(int code, int days) {
        this.code = code;
        this.days = days;
    }

    public int getCode() {
        return code;
    }

    public int getDays() {
        return days;
    }

}
