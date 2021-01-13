package com.group12.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SecondActivity extends AppCompatActivity {
    LinearLayout flight, hotel, trip;
    Button setting, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        flight = findViewById(R.id.flight);
        hotel = findViewById(R.id.hotel);
        trip = findViewById(R.id.trip);
        setting = (Button) findViewById(R.id.setting);
        logout = (Button) findViewById(R.id.logout);

        //trigger flight search screen
        flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(SecondActivity.this, FlightActivity.class));
            }
        });

        //trigger Hotel search screen
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(SecondActivity.this, HotelActivity.class));
            }
        });

        //trigger Trip search screen
        trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, CalendarActivity.class));
            }
        });

        //trigger Setting search screen
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, SettingActivity.class));
            }
        });

        //trigger logout function to exit to login page
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   finish();   }
        });
        logout.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.logout:
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
