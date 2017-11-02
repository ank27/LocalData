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
import com.example.ankurkhandelwal.datahungry.Models.CalendarData;
import com.example.ankurkhandelwal.datahungry.Models.CalendarEventsData;
import com.example.ankurkhandelwal.datahungry.adapter.CalendarAdapter;
import java.util.ArrayList;
import java.util.Date;

public class FragmentCalendar extends Fragment {
    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");
    public static final Uri EVENT_URI = Uri.parse("content://com.android.calendar/events");
    public static final String[] FIELDS = new String[]{"_id", "name", "account_name", "account_type"};
    String TAG = "FragmentCalendar";
    CalendarAdapter adapter;
    ArrayList<CalendarData> calendarArrayList = new ArrayList();
    ArrayList<CalendarEventsData> eventsDataArrayList = new ArrayList();
    RecyclerView inbox_container;
    MarshMallowPermission marshMallowPermission;
    RelativeLayout no_item_layout;
    ProgressBar progress;
    Button refresh;
    RelativeLayout refresh_layout;
    View rootView;
    RelativeLayout topView;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
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
        readCalendarEvents();
        return this.rootView;
    }

    private void readCalendarEvents() {
        this.marshMallowPermission = new MarshMallowPermission(getActivity());
        if (this.marshMallowPermission.checkPermissionForReadCalendar()) {
            Cursor cursor = getActivity().getContentResolver().query(CALENDAR_URI, FIELDS, null, null, null);
            try {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String id = cursor.getString(0);
                        String name = cursor.getString(1);
                        String account_name = cursor.getString(2);
                        Log.d(this.TAG, "CalendarData fields id= " + id + " name= " + name + " account_name = " + account_name + " account_type = " + cursor.getString(3));
                        this.calendarArrayList.add(new CalendarData(id, name, account_name));
                    }
                }
            } catch (AssertionError e) {
            }
            if (this.calendarArrayList.size() > 0) {
                this.eventsDataArrayList = getEventsFromCalendars();
            }
            if (this.calendarArrayList.size() > 0) {
                this.no_item_layout.setVisibility(View.GONE);
                this.topView.setVisibility(View.VISIBLE);
                this.refresh_layout.setVisibility(View.GONE);
                this.adapter = new CalendarAdapter(getActivity(), this.eventsDataArrayList);
                this.inbox_container.setAdapter(this.adapter);
                return;
            }
            this.topView.setVisibility(View.GONE);
            this.refresh_layout.setVisibility(View.GONE);
            this.no_item_layout.setVisibility(View.VISIBLE);
            return;
        }
        this.marshMallowPermission.requestPermissionForReadCalender();
        Log.d(this.TAG, "Permission for calendar not granted");
        this.refresh_layout.setVisibility(View.VISIBLE);
        this.topView.setVisibility(View.GONE);
    }

    private ArrayList<CalendarEventsData> getEventsFromCalendars() {
        ArrayList<CalendarEventsData> eventsDatas = new ArrayList();
        int i = 0;
        while (i < this.calendarArrayList.size()) {
            Cursor eventCursor = getActivity().getContentResolver().query(Uri.parse("content://com.android.calendar/events"), new String[]{"calendar_id", "title", "description", "dtstart", "dtend", "eventLocation"}, null, null, null);
            Log.d(this.TAG, "eventCursor count=" + eventCursor.getCount());
            if (eventCursor.getCount() <= 0 || !eventCursor.moveToFirst()) {
                i++;
            } else {
                do {
                    eventsDatas.add(new CalendarEventsData(eventCursor.getString(0), eventCursor.getString(1), eventCursor.getString(2), ((CalendarData) this.calendarArrayList.get(i)).account_name, new Date(eventCursor.getLong(3)), new Date(eventCursor.getLong(4)), eventCursor.getString(5)));
                } while (eventCursor.moveToNext());
                i++;
            }
        }
        return eventsDatas;
    }

    private void refresh_view() {
        readCalendarEvents();
    }

    public void onResume() {
        super.onResume();
    }
}
