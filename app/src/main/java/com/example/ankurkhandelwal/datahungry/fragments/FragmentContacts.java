package com.example.ankurkhandelwal.datahungry.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
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
import com.example.ankurkhandelwal.datahungry.DataHungryApplication;
import com.example.ankurkhandelwal.datahungry.MarshMallowPermission;
import com.example.ankurkhandelwal.datahungry.Models.Contact;
import com.example.ankurkhandelwal.datahungry.adapter.ContactAdapter;
import java.util.ArrayList;

public class FragmentContacts extends Fragment {
    String TAG = "FragmentContact";
    ContactAdapter adapter;
    ArrayList<Contact> contactArrayList = new ArrayList();
    FloatingActionButton fab_sms;
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
        readContacts();
        return rootView;
    }

    private void readContacts() {
        this.marshMallowPermission = new MarshMallowPermission(getActivity());
        if (this.marshMallowPermission.checkPermissionForReadContact()) {
            Cursor phones = getActivity().getContentResolver().query(Phone.CONTENT_URI, new String[]{"_id", "display_name", "data1", "contact_id"}, null, null, null);
            while (phones.moveToNext()) {
                this.contactArrayList.add(new Contact(phones.getString(phones.getColumnIndex("_id")), phones.getString(phones.getColumnIndex("display_name")), phones.getString(phones.getColumnIndex("data1")), phones.getString(phones.getColumnIndex("contact_id"))));
            }
            if (this.contactArrayList.size() > 0) {
                this.no_item_layout.setVisibility(View.GONE);
                this.topView.setVisibility(View.VISIBLE);
                this.adapter = new ContactAdapter(getActivity(), this.contactArrayList);
                this.inbox_container.setAdapter(this.adapter);
                this.refresh_layout.setVisibility(View.GONE);
                DataHungryApplication.contactList = this.contactArrayList;
                return;
            }else {
                this.topView.setVisibility(View.GONE);
                this.refresh_layout.setVisibility(View.GONE);
                this.no_item_layout.setVisibility(View.VISIBLE);
                return;
            }
        }
        this.marshMallowPermission.requestPermissionForReadContact();
        Log.d(this.TAG, "Permission for contact not granted");
        this.refresh_layout.setVisibility(View.VISIBLE);
        this.topView.setVisibility(View.GONE);
    }

    private void refresh_view() {
        readContacts();
    }

    public void onResume() {
        super.onResume();
    }
}
