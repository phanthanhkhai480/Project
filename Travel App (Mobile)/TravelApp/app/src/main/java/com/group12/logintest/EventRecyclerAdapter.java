package com.group12.logintest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;



public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Events> arrayList;
    DBOpenHelper dbOpenHelper;
    AlertDialog alertDialog;

    static String latest_date = "";

    public EventRecyclerAdapter(Context context, ArrayList<Events> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_event_rowlayout,parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Events events = arrayList.get(position);
        holder.Event.setText(events.getEVENT());
        holder.desc.setText(events.getDESC());
        holder.DateTxt.setText(events.getDATE());
        holder.Time.setText(events.getTIME());
        holder.budget.setText(events.getBudget());
        if(events.getBudget().contains("-")) holder.budget.setVisibility(View.INVISIBLE);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(events.getEVENT().contains("In")) {
                    deleteCalendarEvent(events.getEVENT() + "#" + events.getDESC() + "#" + events.getSibblingEventDate(), events.getDATE(), events.getTIME(), context);
                    deleteCalendarEvent((events.getEVENT() + "#" + events.getDESC() + "#" + events.getDATE()).replaceFirst("In", "Out"), events.getSibblingEventDate(), events.getTIME(), context);
                    arrayList.remove(position);
                    notifyDataSetChanged();
                }else if(events.getEVENT().contains("Out")){
                    deleteCalendarEvent((events.getEVENT() + "#" + events.getDESC() + "#" + events.getDATE()).replaceFirst("Out", "In"), events.getSibblingEventDate(), events.getTIME(), context);
                    deleteCalendarEvent(events.getEVENT() + "#" + events.getDESC() + "#" + events.getSibblingEventDate(), events.getDATE(), events.getTIME(), context);
                    arrayList.remove(position);
                    notifyDataSetChanged();
                }else{
                    deleteCalendarEvent(events.getEVENT() + "#" + events.getDESC() +"#" + events.getBudget(), events.getDATE(), events.getTIME(), context);
                    arrayList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });


        if(events.getEVENT().contains("In") || events.getEVENT().contains("Out") || events.getEVENT().contains("Flight")) holder.dollar.setVisibility(View.INVISIBLE);

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(events.getEVENT()+"#"+events.getDESC()+"#" + events.getBudget(),events.getDATE(),events.getTIME(), context);
                arrayList.remove(position);
                notifyDataSetChanged();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.add_newevent_layout, null);
                final EditText eventName = addView.findViewById(R.id.eventname);
                eventName.setText(events.getEVENT());
                final TextView eventTime = addView.findViewById(R.id.eventtime);
                eventTime.setText(events.getTIME());
                final TextView eventDate = addView.findViewById(R.id.eventdate);
                eventDate.setText(events.getDATE());
                final EditText eventDesc = addView.findViewById(R.id.eventdesc);
                eventDesc.setText(events.getDESC());
                ImageButton setTime = addView.findViewById(R.id.seteventtime);
                Button addEvent = addView.findViewById(R.id.addevent);
                final EditText eventBudget = addView.findViewById(R.id.eventbudget);
                eventBudget.setText(events.getBudget());
                latest_date = events.getDATE();
                setTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                        c.set(Calendar.MINUTE, minute);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat hformat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                        String event_Time = hformat.format(c.getTime());
                                        eventTime.setText(event_Time);
                                    }
                                }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });

                ImageButton setDate = addView.findViewById(R.id.seteventdate);
                final SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                setDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        Date date = null;
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            final String d = eventDateFormat.format(convertStringToDate(events.getDATE()));
                            date = format.parse(d);
                            calendar.setTime(date);
                            Log.e("Date here::", date.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            System.out.println("Error herere");
                        }
                        DatePickerDialog datePickerDialog = new DatePickerDialog(addView.getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                       System.out.println(year + " " + month + " "+ dayOfMonth);
                                       String new_date = year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", dayOfMonth);
                                       eventDate.setText(new_date);
                                        latest_date = new_date;
                                    }
                                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                SimpleDateFormat dateFormat = new SimpleDateFormat( "MMMM yyyy", Locale.ENGLISH);
                SimpleDateFormat monthFormat = new SimpleDateFormat( "MMMM", Locale.ENGLISH);
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                String event_date_text = eventDate.getText().toString();
                System.out.println("event_date_text " + event_date_text);
                final String date = eventDateFormat.format(convertStringToDate(latest_date));
                final String month = monthFormat.format(convertStringToDate(latest_date));
                final String year = yearFormat.format(convertStringToDate(latest_date));
                addEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEvent(eventName.getText().toString() + "#" + eventDesc.getText().toString() + "#" + eventBudget.getText().toString(), eventTime.getText().toString(),date,month,year);
                        //SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();


            }
        });
    }

    private void saveEvent(String event, String time, String date, String month, String year) {
        String event_str = event + " " + time + " " + latest_date +" " + month + " " + year;
        System.out.println("Saving : " + event_str);
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,latest_date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
    }

    private Date convertStringToDate(String eventDate)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try
        {
            date = format.parse(eventDate);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView DateTxt, Event, Time, desc, budget;
        Button delete, modify;
        LinearLayout dollar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DateTxt = itemView.findViewById(R.id.eventdate);
            Event = itemView.findViewById(R.id.eventname);
            Time = itemView.findViewById(R.id.eventime);
            delete = itemView.findViewById(R.id.delete);
            modify = itemView.findViewById(R.id.modify);
            desc = itemView.findViewById(R.id.eventdesc);
            budget = itemView.findViewById(R.id.eventbudget);
            dollar = itemView.findViewById(R.id.dollar);
        }
    }

    private void deleteCalendarEvent(String event, String date, String time, Context context)
    {
        if(event.contains("Flight")){
            String key = event + "@" + date + "@" + time;
            SharedPreferences sharedPref = context.getSharedPreferences("flight_data", Context.MODE_PRIVATE);
            String value = sharedPref.getString(key, "");
            Log.e("key_found", key);
            Log.e("value_found", value);

            dbOpenHelper = new DBOpenHelper(context);
            SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
            dbOpenHelper.deleteEvent(event,date,time,database);
            String[] array = value.split("@");

            try {
                dbOpenHelper.deleteEvent(array[0], array[1], array[2], database);
                dbOpenHelper.close();
            }catch (Exception e){
                e.printStackTrace();
            }


        }
            dbOpenHelper = new DBOpenHelper(context);
            SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
            dbOpenHelper.deleteEvent(event,date,time,database);
            dbOpenHelper.close();

    }

}
