package com.example.ankurkhandelwal.datahungry.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.ankurkhandelwal.datahungry.ContactDetailActivity;
import com.example.ankurkhandelwal.datahungry.DataHungryApplication;
import com.example.ankurkhandelwal.datahungry.Listeners.UpdateSMSAdapterListener;
import com.example.ankurkhandelwal.datahungry.MarshMallowPermission;
import com.example.ankurkhandelwal.datahungry.Models.SMS;
import com.example.ankurkhandelwal.datahungry.adapter.SMSAdapter;
import java.util.ArrayList;

public class FragmentSMS extends Fragment implements UpdateSMSAdapterListener {
    String TAG = "FragmentInbox";
    SMSAdapter adapter;
    FloatingActionButton fab_sms;
    RecyclerView inbox_container;
    UpdateSMSAdapterListener listener;
    MarshMallowPermission marshMallowPermission;
    RelativeLayout no_item_layout;
    ProgressBar progress;
    Button refresh;
    RelativeLayout refresh_layout;
    ArrayList<SMS> smsArrayList = new ArrayList();
    RelativeLayout topView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(this.TAG, "FragmentSMS");
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        this.inbox_container = (RecyclerView) rootView.findViewById(R.id.inbox_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.inbox_container.setLayoutManager(layoutManager);
        this.inbox_container.setHasFixedSize(true);
        this.no_item_layout = (RelativeLayout) rootView.findViewById(R.id.no_item_layout);
        this.topView = (RelativeLayout) rootView.findViewById(R.id.topView);
        this.refresh_layout = (RelativeLayout) rootView.findViewById(R.id.refresh_layout);
        this.refresh = (Button) rootView.findViewById(R.id.refresh);
        this.inbox_container.addItemDecoration(new DividerItemDecoration(this.inbox_container.getContext(), layoutManager.getOrientation()));
        readSMS();
        setListener(this);
        this.refresh.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                readSMS();
            }
        });
        this.fab_sms = (FloatingActionButton) rootView.findViewById(R.id.fab_sms);
        this.fab_sms.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                startContactActivity();
            }
        });
        return rootView;
    }

    private void startContactActivity() {
        Intent contactIntent=new Intent(getActivity(),ContactDetailActivity.class);
        startActivity(contactIntent);
    }


    public void setListener(UpdateSMSAdapterListener listener) {
        this.listener = listener;
    }

    private void readSMS() {
        this.marshMallowPermission = new MarshMallowPermission(getActivity());
        if (this.marshMallowPermission.checkPermissionForReadSMS()) {
            Cursor cur = getActivity().getContentResolver().query(Uri.parse("content://sms/"), new String[]{"DISTINCT address", "_id", "date", "body", "type", "read", "person"}, "address IS NOT NULL) GROUP BY (address", null, null);
            while (cur.moveToNext()) {
                this.smsArrayList.add(new SMS(cur.getString(1), cur.getString(0), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6)));
                DataHungryApplication.smsArrayList = this.smsArrayList;
            }
            Log.d(this.TAG, "smsArrayList count" + this.smsArrayList.size());
            if (this.smsArrayList.size() > 0) {
                this.no_item_layout.setVisibility(View.GONE);
                this.topView.setVisibility(View.VISIBLE);
                this.refresh_layout.setVisibility(View.GONE);
                this.adapter = new SMSAdapter(getActivity(), DataHungryApplication.smsArrayList);
                this.inbox_container.setAdapter(this.adapter);
                return;
            }
            Log.d(this.TAG, "in else");
            this.topView.setVisibility(View.GONE);
            this.no_item_layout.setVisibility(View.VISIBLE);
            this.refresh_layout.setVisibility(View.GONE);
            return;
        }
        Log.d(this.TAG, "Permission for sms not granted");
        this.marshMallowPermission.requestPermissionForReadSMS();
        this.refresh_layout.setVisibility(View.VISIBLE);
        this.no_item_layout.setVisibility(View.GONE);
        this.topView.setVisibility(View.GONE);
    }

    public void updateList(String sender, String msg, String type) {
        SMS sms = new SMS(sender, String.valueOf(System.currentTimeMillis() / 1000), msg, type, String.valueOf(0));
        DataHungryApplication.smsArrayList.add(0, sms);
        DataHungryApplication.smsArrayListFull.add(0, sms);
        this.adapter = new SMSAdapter(getActivity(), DataHungryApplication.smsArrayList);
        this.inbox_container.setAdapter(this.adapter);
    }

    public void checkSMS() {
        this.adapter = new SMSAdapter(getActivity(), DataHungryApplication.smsArrayList);
        this.inbox_container.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
    }

    private void refresh_view() {
        readSMS();
    }

    public void onResume() {
        super.onResume();
        Log.d(this.TAG, "on Resume called");
        checkSMS();
        this.adapter.notifyDataSetChanged();
    }

    public void updateSMSAdapter(String sender, String msg, String type) {
        Log.d(this.TAG, "updateSmsListener called sender =" + sender + "msg = " + msg);
        updateList(sender, msg, type);
    }
}
