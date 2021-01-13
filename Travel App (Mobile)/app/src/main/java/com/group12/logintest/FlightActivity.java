package com.group12.logintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.SeekBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//Activity to implement Search Flight feature.
public class FlightActivity extends AppCompatActivity {

    EditText originPlace, destinationPlace, outboundDate, inboundDate, price_filter;
    Button searchButton, flightLogout, flightBack;
    ImageView img_btn1, img_btn2;
    Boolean out_bound;
    SeekBar seekBar;
    public static ArrayList<Flight> flightArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        originPlace = (EditText) findViewById(R.id.originPlace);
        destinationPlace = (EditText) findViewById(R.id.destinationPlace);
        outboundDate = (EditText) findViewById(R.id.outboundDate);
        inboundDate = (EditText) findViewById(R.id.inboundDate);
        searchButton = (Button) findViewById(R.id.searchFlight);

        flightLogout = (Button) findViewById(R.id.flightLogout);
        flightBack = (Button) findViewById(R.id.flightBack);

        img_btn1 = findViewById(R.id.calendar_btn_1);
        img_btn2 = findViewById(R.id.calendar_btn_2);

        seekBar = findViewById(R.id.seekBar);
        price_filter = findViewById(R.id.price_filter);
        flightArrayList = new ArrayList<>();

        flightLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FlightActivity.this, MainActivity.class));
            }
        });

        flightBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightArrayList.clear();
                validation(originPlace.getText().toString(), destinationPlace.getText().toString(),
                        outboundDate.getText().toString(), inboundDate.getText().toString());

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double price = progress*20;
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
               if(out_bound) outboundDate.setText(date);
               else inboundDate.setText(date);
            }
        };

        img_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out_bound = true;
                new DatePickerDialog(FlightActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        img_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out_bound = false;
                new DatePickerDialog(FlightActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

//Calls skyscanner API to get flight result for the selected pair of cities.
    private void validation(final String origin, final String destination, String outbound, String inbound)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/US/USD/en-US/"+
                        origin+"/"+destination+"/"+outbound+"?inboundPartialDate="+inbound)
                .get()
                .addHeader("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "a88960d2c3msh12cc97b5caad1e2p1783f4jsn5995f341503e")
                .build();

        try{
            Response response = client.newCall(request).execute();
            System.out.println("Response : "+response);
        }catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    FlightActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(myResponse);
                            getQuotesList(myResponse, origin, destination, seekBar.getProgress()*20.0);
                            Intent intent = new Intent(FlightActivity.this, FlightResultActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    //This method parses the json response and creates a list of flightQuote.
    private void getQuotesList(String myResponse, String origin, String dest, Double minPrice) {

        HashMap<Integer, String> carrier_map = new HashMap<>();
        try {
            JSONObject mainObject = new JSONObject(myResponse);
            JSONArray quotesArray = mainObject.getJSONArray("Quotes");
            JSONArray carriersArray = mainObject.getJSONArray("Carriers");
            for(int i = 0; i< carriersArray.length(); i++){
                JSONObject tempObj = carriersArray.getJSONObject(i);
                carrier_map.put(tempObj.getInt("CarrierId"),tempObj.getString("Name"));
            }

            Log.e("Total Flights results: ", String.valueOf(quotesArray.length()));

            //Iterates through JSON Array to parse JSON objects and add them to flightArrayList.
            for(int i = 0; i<quotesArray.length(); i++){
                JSONObject tempObject = quotesArray.getJSONObject(i);
                    int carrier_id = tempObject.getJSONObject("OutboundLeg").getJSONArray("CarrierIds").getInt(0);
                    String carrier = carrier_map.get(carrier_id);
                    String departureDate = tempObject.getJSONObject("OutboundLeg").getString("DepartureDate").replace("00:00:00", "");
                    Double min_price = tempObject.getDouble("MinPrice");
                    Flight flight = new Flight(min_price, departureDate, origin, dest, carrier_id, carrier, inboundDate.getText().toString());
                    if(min_price <= minPrice) flightArrayList.add(flight);
            }

            //If no flights are found.
            if(flightArrayList.size()==0){
                Toast.makeText(this, "No flights found for this route.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "No flights found for this route.", Toast.LENGTH_SHORT).show();

        }

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
                startActivity(new Intent(FlightActivity.this, MainActivity.class));
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

