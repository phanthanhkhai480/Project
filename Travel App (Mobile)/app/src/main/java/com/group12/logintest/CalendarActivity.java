package com.group12.logintest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    CustomCalendarView customCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        try{
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        customCalendarView = (CustomCalendarView)findViewById(R.id.custom_calendar_view);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
