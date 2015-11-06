package cn.hadcn.simplecalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Toast;

import cn.hadcn.calendar.CalendarView;


public class MainActivity extends AppCompatActivity implements CalendarView.OnCalendarListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView calendarView = (CalendarView)findViewById(R.id.view_pager);
        calendarView.setOnCalendarListener(this);
        setTitle(calendarView.getCurrentYear() + "年" + calendarView.getCurrentMonth() + "月");
    }

    @Override
    public void onDayClick(int year, int month, int day) {
        Toast.makeText(this, "click year:" + year + ", month:" + month + ", day:" +day, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChange(int newYear, int newMonth) {
        setTitle(newYear + "年" + newMonth + "月");
    }
}
