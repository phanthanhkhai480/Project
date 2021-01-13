package calendarevents;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(events.getEVENT()+"#"+events.getDESC(),events.getDATE(),events.getTIME());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(events.getEVENT()+"#"+events.getDESC(),events.getDATE(),events.getTIME());
                arrayList.remove(position);
                notifyDataSetChanged();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.add_newevent_layout, null);
                final EditText eventName = addView.findViewById(R.id.eventname);
                eventName.setText(events.getEVENT());
                final TextView eventTime = addView.findViewById(R.id.eventtime);
                eventTime.setText(events.getTIME());
                final EditText eventDesc = addView.findViewById(R.id.eventdesc);
                eventDesc.setText(events.getDESC());
                ImageButton setTime = addView.findViewById(R.id.seteventtime);
                Button addEvent = addView.findViewById(R.id.addevent);
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

                SimpleDateFormat dateFormat = new SimpleDateFormat( "MMMM yyyy", Locale.ENGLISH);
                SimpleDateFormat monthFormat = new SimpleDateFormat( "MMMM", Locale.ENGLISH);
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                final String date = eventDateFormat.format(convertStringToDate(events.getDATE()));
                final String month = monthFormat.format(convertStringToDate(events.getDATE()));
                final String year = yearFormat.format(convertStringToDate(events.getDATE()));
                addEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEvent(eventName.getText().toString() + "#" + eventDesc.getText().toString(), eventTime.getText().toString(),date,month,year);
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
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,database);
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

        TextView DateTxt, Event, Time, desc;
        Button delete, modify;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DateTxt = itemView.findViewById(R.id.eventdate);
            Event = itemView.findViewById(R.id.eventname);
            Time = itemView.findViewById(R.id.eventime);
            delete = itemView.findViewById(R.id.delete);
            modify = itemView.findViewById(R.id.modify);
            desc = itemView.findViewById(R.id.eventdesc);
        }
    }

    private void deleteCalendarEvent(String event, String date, String time)
    {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.deleteEvent(event,date,time,database);
        dbOpenHelper.close();
    }

}
