package com.example.ankurkhandelwal.datahungry.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.Models.CalendarEventsData;
import java.util.List;

public class CalendarAdapter extends Adapter<CalendarAdapter.ViewHolder> {
    Activity activity;
    List<CalendarEventsData> calendarEventsDataArrayList;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView calender_account_name;
        TextView event_date;
        TextView event_desc;
        TextView event_name;
        TextView event_type;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.event_type = (TextView) itemLayoutView.findViewById(R.id.event_type);
            this.event_name = (TextView) itemLayoutView.findViewById(R.id.event_name);
            this.event_date = (TextView) itemLayoutView.findViewById(R.id.event_date);
            this.event_desc = (TextView) itemLayoutView.findViewById(R.id.event_desc);
            this.calender_account_name = (TextView) itemLayoutView.findViewById(R.id.calender_account_name);
        }
    }

    public CalendarAdapter(Activity activity, List<CalendarEventsData> data) {
        this.activity = activity;
        this.calendarEventsDataArrayList = data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.activity.getApplicationContext()).inflate(R.layout.single_event_layout, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.event_type.setText("Event");
        holder.event_name.setText("title : " + ((CalendarEventsData) this.calendarEventsDataArrayList.get(position)).title);
        holder.event_desc.setText("Description : " + ((CalendarEventsData) this.calendarEventsDataArrayList.get(position)).description);
        holder.calender_account_name.setText("account : " + ((CalendarEventsData) this.calendarEventsDataArrayList.get(position)).calendar_account_name);
        holder.event_date.setText("Date : From " + ((CalendarEventsData) this.calendarEventsDataArrayList.get(position)).start_date + " to " + ((CalendarEventsData) this.calendarEventsDataArrayList.get(position)).end_date);
    }

    public int getItemCount() {
        return this.calendarEventsDataArrayList.size();
    }
}
