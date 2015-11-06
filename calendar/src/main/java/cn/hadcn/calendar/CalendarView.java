package cn.hadcn.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * CalendarView
 * Created by 90Chris on 2015/11/6.
 */
public class CalendarView extends ViewPager {
    public static final int START_ITEM = Integer.MAX_VALUE / 2;
    private OnCalendarListener mListener;
    private CalendarPageAdapter mAdapter;
    private int mCurrentYear;
    private int mCurrentMonth;

    public CalendarView(Context context) {
        super(context);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //get the height and with of calendar view, for extending the day item layout
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mAdapter.setLayoutSize(width, height);
    }

    public void setOnCalendarListener(OnCalendarListener listener) {
        mListener = listener;
        mAdapter.setDayListener(new CalendarPageAdapter.OnDayListener() {
            @Override
            public void onDayClick(int day) {
                mListener.onDayClick(mCurrentYear, mCurrentMonth + 1, day);
            }
        });
    }

    private void init(Context context) {
        Calendar now = new GregorianCalendar();
        mCurrentMonth = now.get(Calendar.MONTH);
        mCurrentYear = now.get(Calendar.YEAR);

        mAdapter = new CalendarPageAdapter(context, mCurrentYear, mCurrentMonth);
        setAdapter(mAdapter);
        setCurrentItem(START_ITEM, false);

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentYear = mAdapter.getCurrentYear(position - CalendarView.START_ITEM);
                mCurrentMonth = mAdapter.getCurrentMonth(position - CalendarView.START_ITEM);
                //GregorianCalendar month start from 0
                if ( null != mListener ) {
                    mListener.onMonthChange(mCurrentYear, mCurrentMonth + 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void moveToStartMonth() {
        setCurrentItem(START_ITEM, true);
    }

    public int getCurrentYear() {
        return mCurrentYear;
    }

    public int getCurrentMonth() {
        //GregorianCalendar month start from 0
        return mCurrentMonth + 1;
    }

    public interface OnCalendarListener {
        void onDayClick(int year, int month, int day);
        void onMonthChange(int newYear, int newMonth);
    }
}
