package cn.hadcn.calendar;

/**
 * SimpleDate
 * Created by 90Chris on 2015/11/5.
 */
public class DayItem {
    public enum DayStatus {
        CURRENT_MONTH,
        CURRENT_DAY,
        OTHER
    }

    private int day;
    private DayStatus status;

    public DayItem(int day, DayStatus status) {
        this.day = day;
        this.status = status;
    }

    public int getDay() {
        return day;
    }

    public DayStatus getStatus() {
        return status;
    }
}
