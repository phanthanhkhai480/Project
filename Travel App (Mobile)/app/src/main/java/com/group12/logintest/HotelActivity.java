package com.group12.logintest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HotelActivity extends AppCompatActivity {
    EditText destinationPlace, guests, checkIn, checkOut;
    Button searchHotel, hotelLogout, hotelBack;
    ImageView img_btn1, img_btn2;
    EditText price_filter;
    SeekBar seekBar;
    Boolean out_bound;
    RatingBar starBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        destinationPlace = (EditText) findViewById(R.id.destinationPlace);
        guests = (EditText) findViewById(R.id.guests);
        checkIn = (EditText) findViewById(R.id.checkIn);
        checkOut = (EditText) findViewById(R.id.checkOut);
        searchHotel = (Button) findViewById(R.id.searchHotel);
        hotelLogout = (Button) findViewById(R.id.hotelLogout);
        hotelBack = (Button) findViewById(R.id.hotelBack);

        starBar = findViewById(R.id.ratingBar);
        starBar.setNumStars(5);


        img_btn1 = findViewById(R.id.calendar_btn_1);
        img_btn2 = findViewById(R.id.calendar_btn_2);

        seekBar = findViewById(R.id.seekBar);
        price_filter = findViewById(R.id.price_filter);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double price = progress*5;
                price_filter.setText(String.format("%.2f", price));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        seekBar.setProgress(50);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String date = format1.format(myCalendar.getTime());
                //String date = year + "-" + (String.format("%02", ))
                if(out_bound) checkIn.setText(date);
                else checkOut.setText(date);
            }
        };

        img_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out_bound = true;
                new DatePickerDialog(HotelActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        img_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out_bound = false;
                new DatePickerDialog(HotelActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        hotelLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelActivity.this, MainActivity.class));
            }
        });

        hotelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Opens the HotelResult page and passes the required data as intent extras.
        searchHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (destinationPlace.getText().toString().equals("")) {
                        Toast.makeText(HotelActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(HotelActivity.this, HotelResult.class);
                    System.out.println("Destination here: " + destinationPlace.getText().toString());
                    intent.putExtra("des", destinationPlace.getText().toString());
                    intent.putExtra("checkIn", checkIn.getText().toString());
                    intent.putExtra("checkout", checkOut.getText().toString());
                    intent.putExtra("adults", Integer.parseInt(guests.getText().toString()));
                    intent.putExtra("star", (int) starBar.getRating());
                    intent.putExtra("max_price", Double.parseDouble(price_filter.getText().toString()));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            });
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
                startActivity(new Intent(HotelActivity.this, MainActivity.class));
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}

