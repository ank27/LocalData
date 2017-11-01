package com.example.ankurkhandelwal.datahungry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.Models.SMS;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SMSListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SMS> smsArrayList;

    private class ViewHolder1 {
        public TextView message_text_outgoing;
        public TextView time_text_outgoing;

        private ViewHolder1() {
        }
    }

    private class ViewHolder2 {
        public TextView message_text_incoming;
        public TextView time_text_incoming;

        private ViewHolder2() {
        }
    }

    public SMSListAdapter(ArrayList<SMS> smses, Context context) {
        this.smsArrayList = smses;
        this.context = context;
    }

    public int getCount() {
        return this.smsArrayList.size();
    }

    public Object getItem(int position) {
        return this.smsArrayList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SMS message = (SMS) this.smsArrayList.get(position);
        View v;
        ViewHolder1 holder1;
        Calendar cal;
        Date date;
        if (message.type.equals("2")) {
            v = LayoutInflater.from(this.context).inflate(R.layout.layout_outgoing_sms, parent, false);
            holder1 = new ViewHolder1();
            holder1.message_text_outgoing = (TextView) v.findViewById(R.id.message_text_outgoing);
            holder1.time_text_outgoing = (TextView) v.findViewById(R.id.time_text_outgoing);
            holder1.message_text_outgoing.setText(message.message);
            cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.valueOf(message.time).longValue());
            date = cal.getTime();
            String month_name = new SimpleDateFormat("MMM").format(cal.getTime());
            if (message.status == 0) {
                holder1.time_text_outgoing.setText("Sending...");
                return v;
            } else if (message.status != 1) {
                return v;
            } else {
                holder1.time_text_outgoing.setText(month_name + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + cal.get(5));
                return v;
            }
        } else if (message.type.equals("1")) {
            v = LayoutInflater.from(this.context).inflate(R.layout.layout_incoming_sms, parent, false);
            ViewHolder2 holder2 = new ViewHolder2();
            holder2.message_text_incoming = (TextView) v.findViewById(R.id.message_text_incoming);
            holder2.time_text_incoming = (TextView) v.findViewById(R.id.time_text_incoming);
            holder2.message_text_incoming.setText(message.message);
            cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.valueOf(message.time).longValue());
            date = cal.getTime();
            holder2.time_text_incoming.setText(new SimpleDateFormat("MMM").format(cal.getTime()) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + cal.get(5));
            return v;
        } else if (message.type.equals("5")) {
            v = LayoutInflater.from(this.context).inflate(R.layout.layout_outgoing_sms, parent, false);
            holder1 = new ViewHolder1();
            holder1.message_text_outgoing = (TextView) v.findViewById(R.id.message_text_outgoing);
            holder1.time_text_outgoing = (TextView) v.findViewById(R.id.time_text_outgoing);
            holder1.message_text_outgoing.setPaintFlags(16);
            holder1.message_text_outgoing.setText(message.message);
            cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.valueOf(message.time).longValue());
            date = cal.getTime();
            holder1.time_text_outgoing.setText(new SimpleDateFormat("MMM").format(cal.getTime()) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + cal.get(5));
            return v;
        } else {
            v = LayoutInflater.from(this.context).inflate(R.layout.layout_outgoing_sms, parent, false);
            v.setVisibility(8);
            return v;
        }
    }
}
