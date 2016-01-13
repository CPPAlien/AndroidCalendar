package cn.hadcn.simplecalendar;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.hadcn.calendar.CalendarView;

/**
 *
 * Created by 90Chris on 2016/1/13.
 */
public class CalendarDialog extends DialogFragment implements CalendarView.OnCalendarListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calendar, container, false);
        CalendarView calendarView = (CalendarView)view.findViewById(R.id.view_pager);
        calendarView.setOnCalendarListener(this);
        getDialog().setTitle(calendarView.getCurrentYear() + "年" + calendarView.getCurrentMonth() + "月");
        return view;
    }

    @Override
    public void onDayClick(int year, int month, int day) {
        Toast.makeText(getActivity(), "click year:" + year + ", month:" + month + ", day:" + day, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChange(int newYear, int newMonth) {
        getDialog().setTitle(newYear + "年" + newMonth + "月");
    }
}
