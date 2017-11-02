package com.example.ankurkhandelwal.datahungry.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.MarshMallowPermission;
import com.example.ankurkhandelwal.datahungry.Models.CallLogsData;
import com.example.ankurkhandelwal.datahungry.adapter.CallLogAdapter;
import java.util.ArrayList;

public class FragmentCallLogs extends Fragment {
    String TAG = "FragmentCallLogs";
    CallLogAdapter adapter;
    ArrayList<CallLogsData> callLogArrayList = new ArrayList();
    RecyclerView inbox_container;
    MarshMallowPermission marshMallowPermission;
    RelativeLayout no_item_layout;
    ProgressBar progress;
    Button refresh;
    RelativeLayout refresh_layout;
    View rootView;
    RelativeLayout topView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        this.inbox_container = (RecyclerView) this.rootView.findViewById(R.id.inbox_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.inbox_container.setLayoutManager(layoutManager);
        this.inbox_container.setHasFixedSize(true);
        this.no_item_layout = (RelativeLayout) this.rootView.findViewById(R.id.no_item_layout);
        this.topView = (RelativeLayout) this.rootView.findViewById(R.id.topView);
        this.refresh_layout = (RelativeLayout) this.rootView.findViewById(R.id.refresh_layout);
        this.refresh = (Button) this.rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                refresh_view();
            }
        });
        this.inbox_container.addItemDecoration(new DividerItemDecoration(this.inbox_container.getContext(), layoutManager.getOrientation()));
        readCallLogs();
        return rootView;
    }

    private void readCallLogs() {
        this.marshMallowPermission = new MarshMallowPermission(getActivity());
        if (this.marshMallowPermission.checkPermissionForReadPhoneLogs()) {
            Cursor cur = getActivity().getContentResolver().query(Uri.parse("content://call_log/calls"), null, null, null, null);
            int duration_index = cur.getColumnIndex("duration");
            int type_index = cur.getColumnIndex("type");
            while (cur.moveToNext()) {
                this.callLogArrayList.add(new CallLogsData(cur.getString(0), cur.getString(1), cur.getString(duration_index), Integer.parseInt(cur.getString(type_index))));
            }
            if (this.callLogArrayList.size() > 0) {
                this.no_item_layout.setVisibility(8);
                this.topView.setVisibility(0);
                this.refresh_layout.setVisibility(8);
                this.adapter = new CallLogAdapter(getActivity(), this.callLogArrayList);
                this.inbox_container.setAdapter(this.adapter);
                return;
            }
            this.topView.setVisibility(8);
            this.refresh_layout.setVisibility(8);
            this.no_item_layout.setVisibility(0);
            return;
        }
        this.marshMallowPermission.requestPermissionForReadPhoneLogs();
        Log.d(this.TAG, "Permission for read phone logs not granted");
        this.refresh_layout.setVisibility(0);
        this.topView.setVisibility(8);
    }

    private void refresh_view() {
        readCallLogs();
    }

    public void onResume() {
        super.onResume();
    }
}
