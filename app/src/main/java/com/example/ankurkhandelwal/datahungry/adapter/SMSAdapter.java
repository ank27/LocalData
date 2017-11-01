package com.example.ankurkhandelwal.datahungry.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.Models.SMS;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SMSAdapter extends Adapter<SMSAdapter.ViewHolder> {
    Activity activity;
    List<SMS> smsArrayList;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView sms_content;
        TextView sms_sender;
        TextView sms_timestamp;
        RelativeLayout top_layout;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.sms_sender = (TextView) itemLayoutView.findViewById(R.id.sms_sender);
            this.sms_content = (TextView) itemLayoutView.findViewById(R.id.sms_content);
            this.sms_timestamp = (TextView) itemLayoutView.findViewById(R.id.sms_timestamp);
            this.top_layout = (RelativeLayout) itemLayoutView.findViewById(R.id.top_layout);
        }
    }

    public SMSAdapter(Activity activity, List<SMS> data) {
        this.activity = activity;
        this.smsArrayList = data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.activity.getApplicationContext()).inflate(R.layout.single_sms_layout, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.sms_content.setText("Message : " + ((SMS) this.smsArrayList.get(position)).message);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(((SMS) this.smsArrayList.get(position)).time).longValue());
        Date date = cal.getTime();
        holder.sms_timestamp.setText("Date : " + new SimpleDateFormat("MMM").format(cal.getTime()) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + cal.get(5));
        holder.sms_sender.setText("Sender : " + ((SMS) this.smsArrayList.get(position)).sender);
    }

    public int getItemCount() {
        return this.smsArrayList.size();
    }
}
