package com.group12.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TripActivity extends AppCompatActivity {
    Button tripLogout, tripBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        tripLogout = (Button) findViewById(R.id.tripLogout);
        tripBack = (Button) findViewById(R.id.tripBack);

        //going back button
        tripLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripActivity.this, MainActivity.class));
            }
        });

        tripBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
