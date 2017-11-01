package com.example.ankurkhandelwal.datahungry.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.Models.BrowsingData;
import java.util.List;

public class BrowsingHistoryAdapter extends Adapter<BrowsingHistoryAdapter.ViewHolder> {
    Activity activity;
    List<BrowsingData> browsingDataList;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView tvBoV;
        TextView tvDate;
        TextView tvTitle;
        TextView tvURL;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            TextView tvDate = (TextView) itemLayoutView.findViewById(R.id.date);
            TextView tvTitle = (TextView) itemLayoutView.findViewById(R.id.title);
            TextView tvBoV = (TextView) itemLayoutView.findViewById(R.id.bov);
            TextView tvURL = (TextView) itemLayoutView.findViewById(R.id.url);
        }
    }

    public BrowsingHistoryAdapter(Activity activity, List<BrowsingData> data) {
        this.activity = activity;
        this.browsingDataList = data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.activity.getApplicationContext()).inflate(R.layout.single_browsing_data, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvBoV.setText(((BrowsingData) this.browsingDataList.get(position)).bookmark.equals("1") ? "Bookmarked" : "Visited");
        holder.tvTitle.setText(((BrowsingData) this.browsingDataList.get(position)).title);
        holder.tvURL.setText(((BrowsingData) this.browsingDataList.get(position)).url);
    }

    public int getItemCount() {
        return this.browsingDataList.size();
    }
}
