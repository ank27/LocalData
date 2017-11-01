package com.example.ankurkhandelwal.datahungry.fragments;

import android.content.Intent;
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
import com.example.ankurkhandelwal.datahungry.adapter.CallLogAdapter;
import java.util.ArrayList;

public class FragmentYouTube extends Fragment {
    public static final int RC_SIGN_IN = 0;
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    String TAG = "FragmentYouTube";
    CallLogAdapter adapter;
    ArrayList<String> channelArrayList = new ArrayList();
    RecyclerView inbox_container;
    MarshMallowPermission marshMallowPermission;
    RelativeLayout no_item_layout;
    ProgressBar progress;
    Button refresh;
    RelativeLayout refresh_layout;
    View rootView;
    RelativeLayout topView;

    class C03281 implements OnClickListener {
        C03281() {
        }

        public void onClick(View view) {
            FragmentYouTube.this.refresh_view();
        }
    }

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
        this.refresh.setOnClickListener(new C03281());
        this.inbox_container.addItemDecoration(new DividerItemDecoration(this.inbox_container.getContext(), layoutManager.getOrientation()));
        getYouTubeData();
        return this.rootView;
    }

    private void buildGoogleApiClient() {
    }

    private void getYouTubeData() {
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        Log.d(this.TAG, "requestCode = " + requestCode);
        Log.d(this.TAG, "intent data = " + intent.getData());
        if (requestCode != 1) {
        }
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    private void refresh_view() {
        getYouTubeData();
    }

    public void onResume() {
        super.onResume();
    }
}
