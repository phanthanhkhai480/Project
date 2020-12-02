package calendarevents;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.group12.logintest.R;

public class CalendarActivity extends AppCompatActivity {

    CustomCalendarView customCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCalendarView = (CustomCalendarView)findViewById(R.id.custom_calendar_view);
    }
}
