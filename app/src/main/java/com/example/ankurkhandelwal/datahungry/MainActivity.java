package com.example.ankurkhandelwal.datahungry;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.example.ankurkhandelwal.datahungry.Events.MessageEvent;
import com.example.ankurkhandelwal.datahungry.Receiver.SmsBroadcastReceiver;
import com.example.ankurkhandelwal.datahungry.adapter.ViewPagerAdapter;
import com.example.ankurkhandelwal.datahungry.fragments.FragmentCalendar;
import com.example.ankurkhandelwal.datahungry.fragments.FragmentCallLogs;
import com.example.ankurkhandelwal.datahungry.fragments.FragmentContacts;
import com.example.ankurkhandelwal.datahungry.fragments.FragmentGallery;
import com.example.ankurkhandelwal.datahungry.fragments.FragmentMails;
import com.example.ankurkhandelwal.datahungry.fragments.FragmentSMS;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    ViewPagerAdapter adapter;
    MarshMallowPermission marshMallowPermission;
    SmsBroadcastReceiver receiver = null;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;

    class C06151 implements OnTabSelectedListener {
        C06151() {
        }

        public void onTabSelected(Tab tab) {
            MainActivity.this.viewPager.setCurrentItem(tab.getPosition());
        }

        public void onTabUnselected(Tab tab) {
        }

        public void onTabReselected(Tab tab) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(this.viewPager);
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.viewPager.setCurrentItem(0);
        checkPermissions();
        this.receiver = new SmsBroadcastReceiver();
        this.tabLayout.addOnTabSelectedListener(new C06151());
    }

    private void setupViewPager(ViewPager viewPager) {
        this.adapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.adapter.addFragment(new FragmentSMS(), "SMS");
        this.adapter.addFragment(new FragmentContacts(), "Contacts");
        this.adapter.addFragment(new FragmentCallLogs(), "PhoneLog");
        this.adapter.addFragment(new FragmentCalendar(), "Calendar");
        this.adapter.addFragment(new FragmentGallery(), "Gallery");
        this.adapter.addFragment(new FragmentMails(), "Emails");
        viewPager.setAdapter(this.adapter);
    }

    private void checkPermissions() {
        this.marshMallowPermission = new MarshMallowPermission(this);
        if (this.marshMallowPermission.checkPermissionForReadSMS()) {
            Log.d("Permission", "Already granted for Read Phone SMS");
        } else {
            this.marshMallowPermission.requestPermissionForReadSMS();
        }
        if (this.marshMallowPermission.checkPermissionForReceiveSMS()) {
            Log.d("Permission", "Already granted for Receive SMS");
        } else {
            this.marshMallowPermission.requestPermissionForReceiveSMS();
        }
        if (this.marshMallowPermission.checkPermissionForReadContact()) {
            Log.d("Permission", "Already granted for Read Phone Contact");
        } else {
            this.marshMallowPermission.requestPermissionForReadContact();
        }
    }

    public void onStart() {
        super.onStart();

    }

    public void onStop() {

        super.onStop();
    }

    public void onEvent(MessageEvent event) {
        if (event.event.contains("get_sms")) {
            Log.d(this.TAG, "event in main activity");
        }
    }
}
