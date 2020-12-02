package com.group12.logintest;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//Adapter class for hotel result list.
public class MyHotelListAdapter extends RecyclerView.Adapter<MyHotelListAdapter.ViewHolder> {
    Context context;
    ArrayList<Hotel> hotels;
    SimpleDateFormat dateFormat = new SimpleDateFormat( "MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat( "MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public MyHotelListAdapter(ArrayList<Hotel> hotels, Context context) {
        this.hotels = hotels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.hotel_result_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Hotel  hotel = hotels.get(position);
        holder.hotelName.setText(hotel.getHotelName());
        holder.address.setText(hotel.getAddress());
        holder.price.setText(" Starting $ " + String.format("%.2f", hotel.getPrice()));
        Picasso.get().load(hotel.getImgurl()).into(holder.imageView);
        holder.adults.setText("For "+ hotel.getAdults()+" adults");
        holder.date.setText(hotel.getDate());
        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.book.setEnabled(false);
                String event_Time = "00:00 AM";
                try {
                    Date date_str = new SimpleDateFormat("yyyy-MM-dd").parse(hotel.getCheckIn());

                    final String date = eventDateFormat.format(date_str);
                    final String month = monthFormat.format(date_str);
                    final String year = yearFormat.format(date_str);
                    Date date_str_out = new SimpleDateFormat("yyyy-MM-dd").parse(hotel.getCheckOut());

                    final String date_out = eventDateFormat.format(date_str_out);
                    String name = "Hotel Check-In" + "#" + hotel.getHotelName() +"\n"+ hotel.getAddress()+"#"+date_out;

                    if(doesEventExist(date, name)){
                        Toast.makeText(context, "Hotel already booked for given date.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else saveEvent(name, event_Time.toString(),  date,month,year,context);

                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    Date date_str_in = new SimpleDateFormat("yyyy-MM-dd").parse(hotel.getCheckIn());

                    final String date_out = eventDateFormat.format(date_str_in);
                    Date date_str = new SimpleDateFormat("yyyy-MM-dd").parse(hotel.getCheckOut());

                    final String date = eventDateFormat.format(date_str);
                    final String month = monthFormat.format(date_str);
                    final String year = yearFormat.format(date_str);
                    saveEvent("Hotel Check-Out" + "#" + hotel.getHotelName() + "\n" + hotel.getAddress() +"#"+date_out, event_Time.toString(),  date,month,year,context);

                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView hotelName, address, date, price, adults;
        ImageView imageView;
        Button book;


        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.hotelName = itemView.findViewById(R.id.hotelName);
            this.address = itemView.findViewById(R.id.address);
            this.date = itemView.findViewById(R.id.time);
            this.price = itemView.findViewById(R.id.price);
            this.imageView = itemView.findViewById(R.id.image_view);
            this.adults = itemView.findViewById(R.id.adults);
            this.book = itemView.findViewById(R.id.book);
        }
    }
    private void saveEvent(String event, String time, String date, String month, String year, Context context) {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(context, "Hotel Booking Saved", Toast.LENGTH_SHORT).show();
    }

    private Boolean doesEventExist(String date, String name){
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while(cursor.moveToNext())
        {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            if(event.contains("Hotel"))
                return true;
        }
        cursor.close();
        dbOpenHelper.close();
        return false;
    }
}