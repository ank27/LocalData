package com.example.ankurkhandelwal.datahungry.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
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
import com.example.ankurkhandelwal.datahungry.MarshMallowPermission;
import com.example.ankurkhandelwal.datahungry.Models.Gallery;
import com.example.ankurkhandelwal.datahungry.adapter.GalleryAdapter;
import java.util.ArrayList;
import java.util.Date;

public class FragmentGallery extends Fragment {
    String TAG = "FragmentGallery";
    GalleryAdapter adapter;
    FloatingActionButton fab_sms;
    ArrayList<Gallery> imageArrayList = new ArrayList();
    RecyclerView inbox_container;
    FragmentGallery instance;
    MarshMallowPermission marshMallowPermission;
    RelativeLayout no_item_layout;
    ProgressBar progress;
    Button refresh;
    RelativeLayout refresh_layout;
    RelativeLayout topView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(this.TAG, "FragmentGallery");
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
        getGalleryImaages();
        refresh.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                refresh_view();
            }
        });
        return rootView;
    }

    private void getGalleryImaages() {
        this.marshMallowPermission = new MarshMallowPermission(getActivity());
        if (this.marshMallowPermission.checkPermissionForExternalStorage()) {
            Cursor cursor = getActivity().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name", "datetaken", "description", "mime_type"}, null, null, null);
            while (cursor.moveToNext()) {
                this.imageArrayList.add(new Gallery(cursor.getString(0), cursor.getString(1), new Date(cursor.getLong(2)), cursor.getString(3), cursor.getString(4)));
            }
            if (this.imageArrayList.size() > 0) {
                this.no_item_layout.setVisibility(View.GONE);
                this.topView.setVisibility(View.VISIBLE);
                this.refresh_layout.setVisibility(View.GONE);
                this.adapter = new GalleryAdapter(getActivity(), this.imageArrayList);
                this.inbox_container.setAdapter(this.adapter);
                return;
            }
            this.topView.setVisibility(View.GONE);
            this.no_item_layout.setVisibility(View.VISIBLE);
            this.refresh_layout.setVisibility(View.GONE);
            return;
        }
        Log.d(this.TAG, "Permission for gallery not granted");
        this.marshMallowPermission.requestPermissionForExternalStorage();
        this.refresh_layout.setVisibility(View.VISIBLE);
        this.no_item_layout.setVisibility(View.GONE);
        this.topView.setVisibility(View.GONE);
    }

    private void refresh_view() {
        getGalleryImaages();
    }

    public void onResume() {
        super.onResume();
    }
}
