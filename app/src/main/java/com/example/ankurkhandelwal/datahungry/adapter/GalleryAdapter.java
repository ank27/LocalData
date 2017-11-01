package com.example.ankurkhandelwal.datahungry.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.Models.Gallery;
import java.util.List;

public class GalleryAdapter extends Adapter<GalleryAdapter.ViewHolder> {
    Activity activity;
    List<Gallery> imageArrayList;

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

    public GalleryAdapter(Activity activity, List<Gallery> data) {
        this.activity = activity;
        this.imageArrayList = data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.activity.getApplicationContext()).inflate(R.layout.single_event_layout, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.event_type.setText("type : " + ((Gallery) this.imageArrayList.get(position)).mime_type);
        holder.event_name.setText("name : " + ((Gallery) this.imageArrayList.get(position)).folder_name);
        holder.event_desc.setText("path : " + ((Gallery) this.imageArrayList.get(position)).path);
        holder.calender_account_name.setText("date taken: " + ((Gallery) this.imageArrayList.get(position)).dateTaken);
        holder.event_date.setText("");
    }

    public int getItemCount() {
        return this.imageArrayList.size();
    }
}
