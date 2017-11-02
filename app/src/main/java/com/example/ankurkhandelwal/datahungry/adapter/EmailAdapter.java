package com.example.ankurkhandelwal.datahungry.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ankurkhandelwal.datahungry.DataHungryApplication;
import com.example.ankurkhandelwal.datahungry.Models.Email;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.Models.CalendarEventsData;
import com.example.ankurkhandelwal.datahungry.Utils.PreferencesManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmailAdapter extends Adapter<EmailAdapter.ViewHolder> {
    Activity activity;
    List<Email> emailList;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView email_sender;
        TextView email_date;
        TextView email_snippet;
        TextView email_body;
        TextView email_recipient;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.email_sender = (TextView) itemLayoutView.findViewById(R.id.email_sender);
            this.email_date = (TextView) itemLayoutView.findViewById(R.id.email_date);
            this.email_snippet = (TextView) itemLayoutView.findViewById(R.id.email_snippet);
            this.email_body = (TextView) itemLayoutView.findViewById(R.id.email_body);
            this.email_recipient = (TextView) itemLayoutView.findViewById(R.id.email_recipient);
        }
    }

    public EmailAdapter(Activity activity, List<Email> data) {
        this.activity = activity;
        this.emailList= data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.activity.getApplicationContext()).inflate(R.layout.single_email_layout, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Email email = this.emailList.get(position);
        holder.email_sender.setText("From : "+email.email_sender);
        holder.email_snippet.setText("Subject : "+email.email_snippet);
        holder.email_body.setText("Body : " + email.email_body);

        String dateString = new SimpleDateFormat("dd-MMM-yyyy").format(new Date(email.email_date));
        holder.email_date.setText("Date " + dateString);

        holder.email_recipient.setText(DataHungryApplication.prefs.getString("email_account",""));
    }

    public int getItemCount() {
        return this.emailList.size();
    }
}
