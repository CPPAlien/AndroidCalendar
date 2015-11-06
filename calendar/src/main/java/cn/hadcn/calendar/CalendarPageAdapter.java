package cn.hadcn.calendar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * CalendarPageAdapter
 * Created by 90Chris on 2015/11/6.
 */
public class CalendarPageAdapter extends PagerAdapter implements AdapterView.OnItemClickListener {
    Context mContext;
    private static final int COL_NUM = 7;
    private static final int ROW_NUM = 6;
    Calendar now = new GregorianCalendar();
    int mCurrentYear;
    int mCurrentMonth;
    List<DayItem> mDayItems;
    int mDayItemHeight;
    int mDayItemWidth;
    //when layout size changed, should refresh pagerAdapter and the items of it which have already been loaded
    LoopAdapterSave loopAdapterSave = new LoopAdapterSave();
    int mPreOffset = 0; //for judging left or right scroll, smaller, means left scroll, larger, means right

    public CalendarPageAdapter( Context context, int year, int month ) {
        mContext = context;

        mCurrentYear = year;
        mCurrentMonth = month;
    }

    public void setStartMonth( int year, int month ) {
        mCurrentYear = year;
        mCurrentMonth = month;
    }

    public void setLayoutSize(int width, int height) {
        mDayItemHeight = (int)((height - mContext.getResources().getDimensionPixelOffset(R.dimen.calendar_top_height)) / ROW_NUM + 0.5);
        mDayItemWidth = (int)((width / COL_NUM) + 0.5);
        notifyDataSetChanged();
        loopAdapterSave.refresh();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.
                from(mContext).inflate(R.layout.calendar_page, container, false);
        GridView gridView = (GridView) linearLayout.findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(this);
        int offset = position - CalendarView.START_ITEM;
        mDayItems = getMonthDays(offset);
        MonthViewAdapter adapter = new MonthViewAdapter(mContext, mDayItems, mDayItemWidth, mDayItemHeight);
        gridView.setAdapter(adapter);
        container.addView(linearLayout);
        //loop list save three adapter
        if ( offset - mPreOffset == 0 ) {
            loopAdapterSave.savePre(adapter);
        } else if( offset - mPreOffset == -1 ) {
            loopAdapterSave.saveNext(adapter);
            loopAdapterSave.movePre();
        } else if ( offset - mPreOffset == 1 ){
            loopAdapterSave.savePre(adapter);
            loopAdapterSave.moveNext();
        } else {
            //special dealing to initial three pages
            loopAdapterSave.saveNext(adapter);
        }

        mPreOffset = offset;
        return linearLayout;
    }

    private List<DayItem> getMonthDays(int offset) {
        List<DayItem> dayItems = new ArrayList<>();
        Calendar calendar = new GregorianCalendar(mCurrentYear, mCurrentMonth, 1);
        calendar.add(Calendar.MONTH, offset);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int pointer = 0;
        calendar.add(Calendar.DATE, 1 - dayOfWeek);

        int preMonthStart = calendar.get(Calendar.DATE);

        // add days of before month, but showing in current month
        for ( int i = 0; i < dayOfWeek - 1; ++i ) {
            dayItems.add(new DayItem( preMonthStart + i, DayItem.DayStatus.OTHER ));
            ++pointer;
        }
        // add days of current month
        for ( int i = 0; i < daysOfMonth; ++i ) {
            int day = i + 1;
            if ( now.get(Calendar.YEAR) == currentYear &&
                    now.get(Calendar.MONTH) == currentMonth &&
                    now.get(Calendar.DATE) == day) {
                dayItems.add(new DayItem(day, DayItem.DayStatus.CURRENT_DAY));
            } else {
                dayItems.add(new DayItem(day, DayItem.DayStatus.CURRENT_MONTH));
            }
            ++pointer;
        }
        // add days of next month
        for ( int i = 0; i < ROW_NUM * COL_NUM - pointer; ++i ) {
            dayItems.add(new DayItem(i+1, DayItem.DayStatus.OTHER));
        }
        return dayItems;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //GregorianCalendar month start from 0
        TextView tvDay = (TextView)view.findViewById(R.id.item_day_num);
        if ( null != mListener ) {
            mListener.onDayClick(Integer.parseInt(tvDay.getText().toString()));
        }
    }

    public int getCurrentYear(int offset) {
        Calendar calendar = new GregorianCalendar(mCurrentYear, mCurrentMonth, 1);
        calendar.add(Calendar.MONTH, offset);
        return calendar.get(Calendar.YEAR);
    }

    public int getCurrentMonth(int offset) {
        Calendar calendar = new GregorianCalendar(mCurrentYear, mCurrentMonth, 1);
        calendar.add(Calendar.MONTH, offset);
        return calendar.get(Calendar.MONTH);
    }

    OnDayListener mListener;
    public void setDayListener(OnDayListener listener) {
        mListener = listener;
    }

    private class LoopAdapterSave {
        private MonthViewAdapter[] mMonthViewAdapters = new MonthViewAdapter[3];
        private int pointer = 0;

        public void moveNext() {
            pointer = (pointer + 1) % 3;
        }

        public void movePre() {
            pointer -= 1;
            if ( pointer < 0 ) pointer = 2;
        }

        public void saveNext(MonthViewAdapter adapter) {
            int tmpPointer = ( pointer + 1) % 3;
            mMonthViewAdapters[tmpPointer] = adapter;
        }

        public void savePre(MonthViewAdapter adapter) {
            int tmpPointer = pointer - 1;
            if ( tmpPointer < 0) tmpPointer = 2;
            mMonthViewAdapters[tmpPointer] = adapter;
        }

        public void refresh() {
            // refresh loop cache adapter, like if you change the size of layout, item's size will be changed too
            mMonthViewAdapters[0].setItemSize(mDayItemWidth, mDayItemHeight);
            mMonthViewAdapters[1].setItemSize(mDayItemWidth, mDayItemHeight);
            mMonthViewAdapters[2].setItemSize(mDayItemWidth, mDayItemHeight);
            mMonthViewAdapters[0].notifyDataSetChanged();
            mMonthViewAdapters[1].notifyDataSetChanged();
            mMonthViewAdapters[2].notifyDataSetChanged();
        }
    }

    public interface OnDayListener {
        public void onDayClick(int day);
    }
}
