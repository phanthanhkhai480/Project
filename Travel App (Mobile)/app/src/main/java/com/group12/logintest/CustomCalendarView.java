package com.group12.logintest;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {
    ImageButton NextButton,PreviousButton;
    TextView CurrentDate;
    public static GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat( "MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat( "MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    DBOpenHelper dbOpenHelper;

    MyGridAdapter myGridAdapter;

    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();


    public CustomCalendarView(Context context){
        super(context);
    }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs){

        super(context,attrs);
        this.context = context;

        InitializeLayout();
        SetUpCalendar();

        PreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar.add(Calendar.MONTH,  -1);
                SetUpCalendar();
            }
        });

        NextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, +1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout, null);
                final EditText eventName = addView.findViewById(R.id.eventname);
                final TextView eventTime = addView.findViewById(R.id.eventtime);
                final EditText eventDesc = addView.findViewById(R.id.eventdesc);
                final EditText eventBudget = addView.findViewById(R.id.eventbudget);
                ImageButton setTime = addView.findViewById(R.id.seteventtime);
                Button addEvent = addView.findViewById(R.id.addevent);
                LinearLayout date_view = addView.findViewById(R.id.date_view);
                date_view.setVisibility(GONE);
                setTime.setOnClickListener(new OnClickListener() {
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
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));
                addEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEvent(eventName.getText().toString() + "#" + eventDesc.getText().toString()+"#"+eventBudget.getText().toString(), eventTime.getText().toString(),date,month,year);
                        SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();



            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String date = eventDateFormat.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);

                final EditText budget = showView.findViewById(R.id.budget);
                final TextView budget_text = showView.findViewById(R.id.budget_text);
                final TextView cost = showView.findViewById(R.id.cost);

                final SharedPreferences sh
                        = getContext().getSharedPreferences("MySharedPref",Context.MODE_PRIVATE);
                String s1 = sh.getString(date, "0.00");
                budget.setText(s1);
                ArrayList<Events> events= CollectEventByDate(date);
                double expenses = getTotalSpendings(events);
                cost.setText(String.format("%.2f", expenses));
               try {
                   double budget_double = Double.parseDouble(s1.toString());

                   if (budget_double < expenses) {
                       budget_text.setText("Over-budget");
                       budget_text.setTextColor(Color.RED);
                   }
                   else if (budget_double==expenses){
                       budget_text.setText("Even");
                       budget_text.setTextColor(Color.GRAY);
                   }
                   else {
                       budget_text.setText("Under-budget");
                       budget_text.setTextColor(context.getResources().getColor(R.color.dark_green));
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
                budget.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        SharedPreferences.Editor editor = sh.edit();
                        editor.putString(date, s.toString());
                        editor.apply();

                        try{
                            ArrayList<Events> events= CollectEventByDate(date);
                            double expenses = getTotalSpendings(events);
                            double budget_double = Double.parseDouble(s.toString());
                            cost.setText(String.format("%.2f", expenses));
                            Log.e("Expenses: ", String.valueOf(expenses));

                            if(budget_double < expenses){
                                budget_text.setText("Over-budget");
                                budget_text.setTextColor(Color.RED);
                            }else if(budget_double==expenses){
                                budget_text.setText("Even");
                                budget_text.setTextColor(Color.GRAY);
                            }
                            else {
                                budget_text.setText("Under-budget");
                                budget_text.setTextColor(Color.GREEN);
                            }
                        }catch (Exception e){}

                    }
                });

                RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);

                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext(), CollectEventByDate(date));
                recyclerView.setAdapter(eventRecyclerAdapter);
                eventRecyclerAdapter.notifyDataSetChanged();
                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });
                return true;
            }
        });



    }

    private double getTotalSpendings(ArrayList<Events> events) {
        double expenses = 0;
        for (Events event : events){
            double budget = 0.0;
            try{
                budget = Double.parseDouble(event.getBudget());
            }catch (Exception e){
            }
            expenses+= budget;
        }
        return expenses;
    }

    private ArrayList<Events> CollectEventByDate(String date)
    {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while(cursor.moveToNext())
        {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String Date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event,time,Date,month,Year);
            arrayList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
        return arrayList;
    }


    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void saveEvent(String event, String time, String date, String month, String year) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
    }

    private void InitializeLayout (){

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        NextButton = view.findViewById(R.id.nextBtn);
        PreviousButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridview);

    }

    private void SetUpCalendar(){

        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int firstDay = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDay);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while(dates.size() < MAX_CALENDAR_DAYS)
        {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);

    }

    private void CollectEventsPerMonth(String Month, String year)
    {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month, year, database);
        while(cursor.moveToNext())
        {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));

            Events events = new Events(event, time,date,month, Year);
            eventsList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();
    }


}
