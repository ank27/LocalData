package com.example.ankurkhandelwal.datahungry;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.ankurkhandelwal.datahungry.Listeners.UpdateSMSAdapterListener;
import com.example.ankurkhandelwal.datahungry.Models.SMS;
import com.example.ankurkhandelwal.datahungry.adapter.SMSListAdapter;
import java.util.ArrayList;
import java.util.Collections;

public class ConversationDetailActivity extends AppCompatActivity {
    public String TAG = "ConversationActivity";
    Activity activity;
    SMSListAdapter adapter;
    ListView conversation_container;
    FloatingActionButton fab_send;
    ArrayList<SMS> filteredList = new ArrayList();
    String mobile;
    String name;
    ProgressBar progressConversation;
    UpdateSMSAdapterListener smsAdapterListener;
    BroadcastReceiver smsBroadCastSend = new SMSBroadCastSend();
    EditText sms_edit_text;
    Toolbar toolbarConversation;

    class C03141 implements OnClickListener {
        C03141() {
        }

        public void onClick(View v) {
            ConversationDetailActivity.this.onBackPressed();
        }
    }

    class C03152 implements OnClickListener {
        C03152() {
        }

        public void onClick(View v) {
            if (!ConversationDetailActivity.this.sms_edit_text.getText().toString().equals("")) {
                String contact_number = ConversationDetailActivity.this.mobile;
                try {
                    PendingIntent sentPendingIntent = PendingIntent.getBroadcast(ConversationDetailActivity.this.getApplicationContext(), 0, new Intent("sent"), 134217728);
                    PendingIntent deliverPendingIntent = PendingIntent.getBroadcast(ConversationDetailActivity.this.getApplicationContext(), 0, new Intent("delivered"), 134217728);
                    Log.d(ConversationDetailActivity.this.TAG, " Contact: " + contact_number + " msg: " + ConversationDetailActivity.this.sms_edit_text.getText().toString());
                    SmsManager.getDefault().sendTextMessage(contact_number, null, ConversationDetailActivity.this.sms_edit_text.getText().toString(), sentPendingIntent, deliverPendingIntent);
                    SMS smsObject = new SMS(String.valueOf(System.currentTimeMillis()), ConversationDetailActivity.this.sms_edit_text.getText().toString(), String.valueOf(2), String.valueOf(1), 0);
                    if (ConversationDetailActivity.this.filteredList.size() > 0) {
                        Log.d(ConversationDetailActivity.this.TAG, "filterlist size>0 " + ConversationDetailActivity.this.filteredList.size());
                        ConversationDetailActivity.this.filteredList.add(smsObject);
                        ConversationDetailActivity.this.adapter.notifyDataSetChanged();
                        ConversationDetailActivity.this.conversation_container.setSelection(ConversationDetailActivity.this.adapter.getCount() - 1);
                    } else {
                        Log.d(ConversationDetailActivity.this.TAG, "filterlist size " + ConversationDetailActivity.this.filteredList.size());
                        ConversationDetailActivity.this.filteredList.add(smsObject);
                        ConversationDetailActivity.this.adapter = new SMSListAdapter(ConversationDetailActivity.this.filteredList, ConversationDetailActivity.this.activity);
                        ConversationDetailActivity.this.conversation_container.setAdapter(ConversationDetailActivity.this.adapter);
                        ConversationDetailActivity.this.adapter.notifyDataSetChanged();
                        ConversationDetailActivity.this.conversation_container.setSelection(ConversationDetailActivity.this.adapter.getCount() - 1);
                        Log.d(ConversationDetailActivity.this.TAG, "Size now " + ConversationDetailActivity.this.filteredList.size());
                    }
                    ConversationDetailActivity.this.sms_edit_text.getText().clear();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ConversationDetailActivity.this.getApplicationContext(), "Failed to send sms", 1).show();
                }
            }
        }
    }

    public class SMSBroadCastSend extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String result = "";
            switch (getResultCode()) {
                case -1:
                    result = "SMS sent successfully";
                    SMS sms = (SMS) ConversationDetailActivity.this.filteredList.get(ConversationDetailActivity.this.filteredList.size() - 1);
                    sms.status = 1;
                    ConversationDetailActivity.this.adapter.notifyDataSetChanged();
                    ConversationDetailActivity.this.conversation_container.setSelection(ConversationDetailActivity.this.adapter.getCount() - 1);
                    Log.d(ConversationDetailActivity.this.TAG, " Sender " + ConversationDetailActivity.this.mobile + " message " + sms.message);
                    SMS sms_save = new SMS(ConversationDetailActivity.this.mobile, String.valueOf(System.currentTimeMillis() / 1000), sms.message, "2", String.valueOf(0));
                    DataHungryApplication.smsArrayList.add(0, sms_save);
                    DataHungryApplication.smsArrayListFull.add(0, sms_save);
                    break;
                case 1:
                    result = "Transmission failed";
                    break;
                case 2:
                    result = "Radio off";
                    break;
                case 3:
                    result = "No PDU defined";
                    break;
                case 4:
                    result = "No service";
                    break;
            }
            Toast.makeText(ConversationDetailActivity.this.getApplicationContext(), result, 1).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_conversation);
        this.toolbarConversation = (Toolbar) findViewById(R.id.toolbarConversation);
        this.toolbarConversation.setNavigationIcon((int) R.drawable.ic_arrow_back_white_24dp);
        this.toolbarConversation.setNavigationOnClickListener(new C03141());
        this.activity = this;
        this.progressConversation = (ProgressBar) findViewById(R.id.progressConversation);
        this.conversation_container = (ListView) findViewById(R.id.conversation_container);
        this.sms_edit_text = (EditText) findViewById(R.id.sms_edit_text);
        this.fab_send = (FloatingActionButton) findViewById(R.id.fab_send);
        this.name = getIntent().getExtras().getString("name");
        this.mobile = getIntent().getExtras().getString("mobile");
        this.toolbarConversation.setTitle(this.name);
        check_conversation(this.mobile);
        setListner(this.smsAdapterListener);
        this.fab_send.setOnClickListener(new C03152());
    }

    private void check_conversation(String mobile) {
        this.progressConversation.setVisibility(View.VISIBLE);
        mobile = mobile.replaceAll("\\s+", "");
        Log.d("Check conv", "of " + mobile);
        this.filteredList = new ArrayList();
        Cursor cur = getContentResolver().query(Uri.parse("content://sms/"), new String[]{"date", "body", "type", "read"}, "address='" + mobile + "'", null, "date desc");
        String sms = "";
        while (cur.moveToNext()) {
            this.filteredList.add(new SMS(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), 1));
        }
        if (this.filteredList.size() > 0) {
            Collections.reverse(this.filteredList);
            this.adapter = new SMSListAdapter(this.filteredList, this.activity);
            this.conversation_container.setAdapter(this.adapter);
            this.conversation_container.setSelection(this.adapter.getCount() - 1);
        }
        this.progressConversation.setVisibility(View.GONE);
    }

    public void setListner(UpdateSMSAdapterListener listner) {
        this.smsAdapterListener = listner;
    }

    public void onResume() {
        super.onResume();
        registerReceiver(this.smsBroadCastSend, new IntentFilter("sent"));
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(this.smsBroadCastSend);
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
