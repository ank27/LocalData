package com.example.ankurkhandelwal.datahungry.fragments;

import android.accounts.Account;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ankurkhandelwal.datahungry.ContactDetailActivity;
import com.example.ankurkhandelwal.datahungry.DataHungryApplication;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.MailActivity;
import com.example.ankurkhandelwal.datahungry.MarshMallowPermission;
import com.example.ankurkhandelwal.datahungry.Models.SMS;
import com.example.ankurkhandelwal.datahungry.Utils.PreferencesManager;
import com.example.ankurkhandelwal.datahungry.adapter.CalendarAdapter;
import com.example.ankurkhandelwal.datahungry.adapter.EmailAdapter;
import com.example.ankurkhandelwal.datahungry.adapter.SMSAdapter;
import java.util.ArrayList;

public class FragmentMails extends Fragment {
    String TAG = "FragmentMails";
    FloatingActionButton fab_email;
    RecyclerView inbox_container;
    RelativeLayout no_item_layout;
    Button refresh;
    RelativeLayout refresh_layout;
    RelativeLayout topView;
    EmailAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(this.TAG, "FragmentMails");
        View rootView = inflater.inflate(R.layout.fragment_email, container, false);
        this.inbox_container = (RecyclerView) rootView.findViewById(R.id.inbox_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.inbox_container.setLayoutManager(layoutManager);
        this.inbox_container.setHasFixedSize(true);
        this.no_item_layout = (RelativeLayout) rootView.findViewById(R.id.no_item_layout);
        this.topView = (RelativeLayout) rootView.findViewById(R.id.topView);
        this.refresh_layout = (RelativeLayout) rootView.findViewById(R.id.refresh_layout);
        this.refresh = (Button) rootView.findViewById(R.id.refresh);
        this.refresh.setText("Get Email");
        fab_email = (FloatingActionButton) rootView.findViewById(R.id.fab_email);
        checkMails();
        this.refresh.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                readMails();
            }
        });

        fab_email.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                openSendEmailDialog();
            }
        });

        this.inbox_container.addItemDecoration(new DividerItemDecoration(this.inbox_container.getContext(), layoutManager.getOrientation()));
        this.refresh.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                readMails();
            }
        });
        return rootView;
    }

    private void openSendEmailDialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.dialog_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        final EditText recipient = (EditText) promptsView.findViewById(R.id.recipient);
        final EditText subject = (EditText) promptsView.findViewById(R.id.subject);
        final EditText body = (EditText) promptsView.findViewById(R.id.body);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(recipient.getText().toString().matches("") || subject.getText().toString().matches("") || body.getText().toString().matches("")){
                                    Toast.makeText(getActivity().getApplicationContext(),"Please fill all fields",Toast.LENGTH_LONG).show();
                                }else{
                                    Boolean isEmailValid = isValidEmail(recipient.getText().toString());
                                    if(isEmailValid){

                                    }else {
                                        Toast.makeText(getActivity().getApplicationContext(),"Email is not valid",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

//    public static MimeMessage createEmail(String to,
//                                          String from,
//                                          String subject,
//                                          String bodyText)
//            throws MessagingException {
//        Properties props = new Properties();
//        Session session = Session.getDefaultInstance(props, null);
//
//        MimeMessage email = new MimeMessage(session);
//
//        email.setFrom(new InternetAddress(from));
//        email.addRecipient(javax.mail.Message.RecipientType.TO,
//                new InternetAddress(to));
//        email.setSubject(subject);
//        email.setText(bodyText);
//        return email;
//    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checkMails() {
        if (DataHungryApplication.emailArrayList.size() == 0){
            refresh_layout.setVisibility(View.VISIBLE);
            inbox_container.setVisibility(View.GONE);
            fab_email.setVisibility(View.GONE);
        }else {
            refresh_layout.setVisibility(View.GONE);
            inbox_container.setVisibility(View.VISIBLE);
            fab_email.setVisibility(View.VISIBLE);
            this.adapter = new EmailAdapter(getActivity(), DataHungryApplication.emailArrayList);
            this.inbox_container.setAdapter(this.adapter);
        }
    }

    private void readMails() {
        startActivity(new Intent(getActivity(), MailActivity.class));
    }

    private void refresh_view() {
        readMails();
    }

    public void onResume() {
        super.onResume();
        checkMails();
    }
}
