package com.example.ankurkhandelwal.datahungry.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.Models.CallLogsData;
import java.util.List;

public class CallLogAdapter extends Adapter<CallLogAdapter.ViewHolder> {
    Activity activity;
    List<CallLogsData> callLogsDataList;

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

    public CallLogAdapter(Activity activity, List<CallLogsData> data) {
        this.activity = activity;
        this.callLogsDataList = data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.activity.getApplicationContext()).inflate(R.layout.single_sms_layout, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (((CallLogsData) this.callLogsDataList.get(position)).in_or_out) {
            case 1:
                holder.sms_content.setText("type : Incoming Call");
                break;
            case 2:
                holder.sms_content.setText("type : Outgoing Call");
                break;
            case 3:
                holder.sms_content.setText("type : Missed Call");
                break;
        }
        long duration = Long.parseLong(((CallLogsData) this.callLogsDataList.get(position)).duration);
        int hours = ((int) duration) / 3600;
        int remainder = (int) (duration - ((long) (hours * 3600)));
        int mins = remainder / 60;
        int secs = remainder - (mins * 60);
        String timeCallDuration = "";
        if (hours != 0) {
            timeCallDuration = timeCallDuration + hours + " h ";
        }
        if (mins != 0) {
            timeCallDuration = timeCallDuration + mins + " m ";
        }
        holder.sms_timestamp.setText("Duration : " + (timeCallDuration + secs + " seconds"));
        holder.sms_sender.setText("contact : " + ((CallLogsData) this.callLogsDataList.get(position)).name);
    }

    public int getItemCount() {
        return this.callLogsDataList.size();
    }
}
