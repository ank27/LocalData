package com.example.ankurkhandelwal.datahungry.fragments;

import android.content.Intent;
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
import com.example.ankurkhandelwal.datahungry.MailActivity;
import com.example.ankurkhandelwal.datahungry.MarshMallowPermission;
import com.example.ankurkhandelwal.datahungry.Models.SMS;
import com.example.ankurkhandelwal.datahungry.adapter.SMSAdapter;
import java.util.ArrayList;

public class FragmentMails extends Fragment {
    String TAG = "FragmentMails";
    SMSAdapter adapter;
    FloatingActionButton fab_sms;
    RecyclerView inbox_container;
    FragmentMails instance;
    MarshMallowPermission marshMallowPermission;
    RelativeLayout no_item_layout;
    ProgressBar progress;
    Button refresh;
    RelativeLayout refresh_layout;
    ArrayList<SMS> smsArrayList = new ArrayList();
    RelativeLayout topView;

    class C03251 implements OnClickListener {
        C03251() {
        }

        public void onClick(View view) {
            FragmentMails.this.refresh_view();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(this.TAG, "FragmentMails");
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        this.inbox_container = (RecyclerView) rootView.findViewById(R.id.inbox_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.inbox_container.setLayoutManager(layoutManager);
        this.inbox_container.setHasFixedSize(true);
        this.no_item_layout = (RelativeLayout) rootView.findViewById(R.id.no_item_layout);
        this.topView = (RelativeLayout) rootView.findViewById(R.id.topView);
        this.refresh_layout = (RelativeLayout) rootView.findViewById(R.id.refresh_layout);
        this.refresh = (Button) rootView.findViewById(R.id.refresh);
        this.inbox_container.addItemDecoration(new DividerItemDecoration(this.inbox_container.getContext(), layoutManager.getOrientation()));
        readMails();
        this.refresh.setOnClickListener(new C03251());
        return rootView;
    }

    private void readMails() {
        startActivity(new Intent(getActivity(), MailActivity.class));
    }

    private void refresh_view() {
        readMails();
    }

    public void onResume() {
        super.onResume();
    }
}
