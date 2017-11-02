package com.example.ankurkhandelwal.datahungry;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.ankurkhandelwal.datahungry.Listeners.ContactFilterListener;
import com.example.ankurkhandelwal.datahungry.Models.Contact;
import com.example.ankurkhandelwal.datahungry.adapter.ContactAdapter;
import java.util.ArrayList;

public class ContactDetailActivity extends AppCompatActivity implements ContactFilterListener {
    public String TAG = "ContactDetailActivity";
    Activity activity;
    ContactAdapter adapter;
    ArrayList<Contact> contactList = new ArrayList();
    public RelativeLayout contactListLayout;
    RecyclerView contact_container;
    Button done_btn;
    public EditText editPerson;
    public FloatingActionButton fab_send_sms;
    private TextWatcher filterTextWatcher = new C03134();
    public RelativeLayout no_item_layout;
    public ProgressBar progressSend;
    Toolbar toolbarSendSms;


    class C03112 implements OnClickListener {
        C03112() {
        }

        public void onClick(View v) {
            if (ContactDetailActivity.this.editPerson.getText().toString().equals("")) {
                Toast.makeText(ContactDetailActivity.this.getApplicationContext(), "Please select a contact person", 0).show();
                return;
            }
            Intent conversationIntent = new Intent(ContactDetailActivity.this, ConversationDetailActivity.class);
            if (DataHungryApplication.selected_contact != null) {
                conversationIntent.putExtra("name", DataHungryApplication.selected_contact.name);
                conversationIntent.putExtra("mobile", DataHungryApplication.selected_contact.mobile);
            } else {
                conversationIntent.putExtra("name", ContactDetailActivity.this.editPerson.getText().toString());
                conversationIntent.putExtra("mobile", ContactDetailActivity.this.editPerson.getText().toString());
            }
            ContactDetailActivity.this.startActivity(conversationIntent);
        }
    }

    class C03123 implements OnClickListener {
        C03123() {
        }

        public void onClick(View v) {
            ContactDetailActivity.this.fab_send_sms.performClick();
        }
    }

    class C03134 implements TextWatcher {
        C03134() {
        }

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence entered_string, int start, int before, int count) {
            ContactDetailActivity.this.adapter.getFilter().filter(entered_string);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_contact);
        this.toolbarSendSms = (Toolbar) findViewById(R.id.toolbarSendSms);
        this.toolbarSendSms.setNavigationIcon((int) R.drawable.ic_arrow_back_white_24dp);
        this.editPerson = (EditText) findViewById(R.id.editPerson);
        this.editPerson.addTextChangedListener(this.filterTextWatcher);
        this.toolbarSendSms.setNavigationOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                ContactDetailActivity.this.onBackPressed();
            }
        });
        this.done_btn = (Button) findViewById(R.id.done_btn);
        this.activity = this;
        this.progressSend = (ProgressBar) findViewById(R.id.progressSend);
        this.contactListLayout = (RelativeLayout) findViewById(R.id.contactListLayout);
        this.no_item_layout = (RelativeLayout) findViewById(R.id.no_item_layout);
        this.fab_send_sms = (FloatingActionButton) findViewById(R.id.fab_send_sms);
        this.fab_send_sms.setOnClickListener(new C03112());
        this.done_btn.setOnClickListener(new C03123());
        if (VERSION.SDK_INT < 21) {
            LayoutParams p = (LayoutParams) this.fab_send_sms.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            this.fab_send_sms.setLayoutParams(p);
        }
        this.contact_container = (RecyclerView) findViewById(R.id.contact_container);
        this.contact_container.setLayoutManager(new LinearLayoutManager(this));
        this.contact_container.setHasFixedSize(true);
        getContacts();
    }

    private void getContacts() {
        this.adapter = new ContactAdapter(this.activity, DataHungryApplication.contactList, this);
        this.contact_container.setAdapter(this.adapter);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.editPerson.removeTextChangedListener(this.filterTextWatcher);
    }

    public void showLayout(int layout_type) {
        if (layout_type == 1) {
            this.no_item_layout.setVisibility(View.GONE);
            this.contactListLayout.setVisibility(View.VISIBLE);
            this.progressSend.setVisibility(View.GONE);
        } else if (layout_type == 0) {
            this.no_item_layout.setVisibility(View.VISIBLE);
            this.contactListLayout.setVisibility(View.GONE);
            this.progressSend.setVisibility(View.GONE);
        }
    }

    public void onContactClick(Contact contact) {
        this.editPerson.setText("");
        this.editPerson.setText(contact.mobile);
        this.editPerson.setSelection(this.editPerson.length());
        DataHungryApplication.selected_contact = contact;
        this.fab_send_sms.performClick();
    }
}
