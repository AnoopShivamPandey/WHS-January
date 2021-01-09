package com.apk_home_service.customer.UI.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.DateTimeHandler;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import java.util.Date;

public class DateChooseActivity extends Activity {

    CustomCalendarView calendarView;


    CalendarListener calendarListener = new CalendarListener() {
        @Override
        public void onDateSelected(Date date) {
//            Thu Nov 16 18:28:23 GMT+05:30 2017
            String dateStr = DateTimeHandler.changeDateToString(date, DateTimeHandler.DATE_FORMATE_5);
            CommonUtill.print("calendarListener onDateSelected==" + dateStr);
            Intent intent = new Intent();
            intent.putExtra("date", dateStr);
            setResult(RESULT_OK, intent);
            finish();

        }

        @Override
        public void onMonthChanged(Date time) {
            CommonUtill.print("calendarListener onMonthChanged==" + time);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_choose);

        calendarView = (CustomCalendarView) findViewById(R.id.calendarView);

//        calendarView.setCurrentDayOfMonth(getResources().getColor(R.color.colorPrimary));
//        calendarView.setCurrentDayOfMonthTxt(getResources().getColor(R.color.textColorLight));
//        calendarView.setCalendarBackgroundColor(getResources().getColor(R.color.appBgColor));

        calendarView.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp, R.drawable.ic_navigate_next_black_24dp);
        calendarView.setShowOverflowDate(false);

//        if (getIntent().hasExtra("minumumDate")) {
//            calendarView.setMinimumDateLimit(new Date());
//        }
//        if (getIntent().hasExtra("maximumDate")) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.DAY_OF_MONTH, 7);
//            calendarView.setMaximumDateLimit(calendar.getTime());
//        }

        calendarView.refreshCalendar();
        calendarView.setCalendarListener(calendarListener);

    }
}
