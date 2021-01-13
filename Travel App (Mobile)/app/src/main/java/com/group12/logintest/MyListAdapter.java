package com.group12.logintest;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;


//Adapter class for flight result list.
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    ArrayList<Flight> flightQuotes;
    SimpleDateFormat monthFormat = new SimpleDateFormat( "MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Context context;

    public MyListAdapter(ArrayList<Flight> flightQuotes, Context context) {
        this.flightQuotes = flightQuotes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.flight_result_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Flight flightQuote = flightQuotes.get(position);
        holder.time.setText("Departure: "+ flightQuote.departureDate.replace("T", "  "));
        holder.price.setText("$ " + flightQuote.minPrice);
        holder.source.setText(flightQuote.origin);
        holder.destination.setText(flightQuote.destination);
        holder.airline.setText(flightQuote.CarrierName);
        holder.return_time.setText("Return: "+ flightQuote.returnDate);

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = getRandomNumberInRange(0,11);
                int min = getRandomNumberInRange(0,59);
                holder.book.setEnabled(false);
                String a;
                if(hour%2 == 0) a = "AM";
                else a = "PM";
                String event_Time = String.format("%02d", hour) + ":"+ String.format("%02d",min) + " "+a;
                String event_one="";
                String event_two ="";
                try {
                    Date date_str = new SimpleDateFormat("yyyy-MM-dd").parse(flightQuote.departureDate.replace("T", "  "));
                    final String date = eventDateFormat.format(date_str);
                    final String month = monthFormat.format(date_str);
                    final String year = yearFormat.format(date_str);
                    if(doesEventExist(date)){
                        Toast.makeText(context, "Flight already booked for given date.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        String event_title = "Flight Booking" + "#" +flightQuote.CarrierName+"\n"+flightQuote.origin +" to "+ flightQuote.destination+"#";
                        saveEvent(event_title, event_Time.toString(),  date,month,year,context);
                        event_one = event_title + "@" + date + "@" + event_Time;
                    }

                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                hour = getRandomNumberInRange(1,11);
                min = getRandomNumberInRange(0,59);
                if(hour%2 == 0) a = "AM";
                else a = "PM";
                event_Time = String.format("%02d", hour) + ":"+ String.format("%02d",min) + " "+a;

                try {
                    Date date_str = new SimpleDateFormat("yyyy-MM-dd").parse(flightQuote.returnDate);

                    final String date = eventDateFormat.format(date_str);
                    final String month = monthFormat.format(date_str);
                    final String year = yearFormat.format(date_str);
                    String event_title = "Flight Booking" + "#" +flightQuote.CarrierName+"\n"+flightQuote.destination +" to "+ flightQuote.origin+"#";
                    saveEvent(event_title, event_Time.toString(),  date,month,year,context);
                    event_two = event_title + "@" + date + "@" + event_Time;
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                saveEvents(event_one, event_two, context);
            }
        });
    }

    private void saveEvents(String event_one, String event_two, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("flight_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(event_one,event_two);
        editor.putString(event_two, event_one);
        editor.apply();
        Log.e("key-->", event_one);
        Log.e("value-->", event_two);
    }

    @Override
    public int getItemCount() {
        return flightQuotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView airline, source, destination, price, time, return_time;
        Button book;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.airline = itemView.findViewById(R.id.airline);
            this.source = itemView.findViewById(R.id.source);
            this.destination = itemView.findViewById(R.id.destination);
            this.price = itemView.findViewById(R.id.price);
            this.time = itemView.findViewById(R.id.time);
            this.return_time = itemView.findViewById(R.id.return_time);
            this.book = itemView.findViewById(R.id.book);
        }
    }
    private void saveEvent(String event, String time, String date, String month, String year, Context context) {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(context, "Flight Booking Saved", Toast.LENGTH_SHORT).show();
    }

    private Boolean doesEventExist(String date){
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while(cursor.moveToNext())
        {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            if(event.contains("Flight"))
                return true;

        }
        cursor.close();
        dbOpenHelper.close();
        return false;
    }
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}