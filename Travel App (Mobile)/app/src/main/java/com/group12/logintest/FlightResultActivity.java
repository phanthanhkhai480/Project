package com.group12.logintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import static com.group12.logintest.FlightActivity.flightArrayList;

public class FlightResultActivity extends AppCompatActivity {

    MyListAdapter myListAdapter;
    RecyclerView responseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        responseView = findViewById(R.id.recycler_view);
        myListAdapter = new MyListAdapter(flightArrayList, this);
        responseView.setAdapter(myListAdapter);
        responseView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
