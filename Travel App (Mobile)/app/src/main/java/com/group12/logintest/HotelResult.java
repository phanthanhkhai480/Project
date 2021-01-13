package com.group12.logintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HotelResult extends AppCompatActivity {

    RecyclerView recyclerView;
    MyHotelListAdapter myHotelListAdapter;
    ArrayList<Hotel> hotels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_result);
        recyclerView = findViewById(R.id.recycler_view);
        hotels = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String destinationName = getIntent().getStringExtra("des");
        String checkIn = getIntent().getStringExtra("checkIn");
        String checkOut = getIntent().getStringExtra("checkout");
        int adults = getIntent().getIntExtra("adults", 1);
        int star = getIntent().getIntExtra("star", 1);
        double max_price = getIntent().getDoubleExtra("max_price",100.00);

        System.out.println("destinationName:"+destinationName);

        myHotelListAdapter = new MyHotelListAdapter(hotels,HotelResult.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(HotelResult.this));
        recyclerView.setAdapter(myHotelListAdapter);
        getPlaceId(destinationName,checkIn,checkOut,adults,star, max_price);

    }

    //Get place id from place name.
    private void getPlaceId(final String destinationName, final String checkInDate, final String checkoutDate, final int adults, final int star, final double max_price){

        String url = "https://hotels4.p.rapidapi.com/locations/search?locale=en_US&query="+destinationName;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "hotels4.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "c818638c80msh29cee8001e69163p137235jsn863205b7d988")
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
                    HotelResult.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(myResponse);
                            int destinationId;
                            try {
                                JSONObject mainObject = new JSONObject(myResponse);
                                destinationId =((JSONObject)((JSONObject) mainObject.getJSONArray("suggestions").get(0)).getJSONArray("entities").get(0)).getInt("destinationId");
                                //Call the hotels4 API to get list of hotels for the required destinationId.
                                findHotels(destinationId, checkInDate,checkoutDate,adults,star, max_price);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

    //Get list of hotels from hotels4 API. This methods calls the API and parses the JSON response to get list of hotels.
    private void findHotels(int destinationId, final String checkInDate, final String checkoutDate, final int adults, int star, final double max_price)  {

        String url = "https://hotels4.p.rapidapi.com/properties/list?currency=USD&starRatings="+ star +"%252C3&checkIn="+checkInDate+"&locale=en_US&checkOut="+checkoutDate+"&sortOrder=PRICE&destinationId="+destinationId+"&type=CITY&pageNumber=1&pageSize=100&adults1="+adults;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "hotels4.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "c818638c80msh29cee8001e69163p137235jsn863205b7d988")
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
                    HotelResult.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray searchResults = new JSONObject(myResponse).getJSONObject("data").getJSONObject("body").getJSONObject("searchResults").getJSONArray("results");
                                hotels.clear();
                                for(int i = 0; i<searchResults.length(); i++){
                                    try {
                                        JSONObject tempObj = searchResults.getJSONObject(i);
                                        int id = tempObj.getInt("id");
                                        String name = tempObj.getString("name");
                                        String url = tempObj.getString("thumbnailUrl");
                                        int starRating = tempObj.getInt("starRating");
                                        String address = tempObj.getJSONObject("address").getString("streetAddress");
                                        Double price = tempObj.getJSONObject("ratePlan").getJSONObject("price").getDouble("exactCurrent");
                                        Hotel hotel = new Hotel(id, name, price, starRating, address, url, adults, "From " + checkInDate + " to " + checkoutDate);
                                       if(price<=max_price) hotels.add(hotel);
                                        myHotelListAdapter.notifyDataSetChanged();
                                    }catch (Exception e){e.printStackTrace();}
                                }
                                if(hotels.size()==0)
                                    Toast.makeText(HotelResult.this, "No hotel found for the selected filters.", Toast.LENGTH_SHORT).show();
                                System.out.println("###" + hotels.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(HotelResult.this, "No hotel found for the selected filters.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
