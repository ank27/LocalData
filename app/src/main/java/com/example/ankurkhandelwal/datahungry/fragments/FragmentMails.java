package com.example.ankurkhandelwal.datahungry.fragments;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.example.ankurkhandelwal.datahungry.MainActivity;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.MailActivity;
import com.example.ankurkhandelwal.datahungry.MarshMallowPermission;
import com.example.ankurkhandelwal.datahungry.Models.SMS;
import com.example.ankurkhandelwal.datahungry.Utils.PreferencesManager;
import com.example.ankurkhandelwal.datahungry.adapter.CalendarAdapter;
import com.example.ankurkhandelwal.datahungry.adapter.EmailAdapter;
import com.example.ankurkhandelwal.datahungry.adapter.SMSAdapter;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class FragmentMails extends Fragment {
    String TAG = "FragmentMails";
    FloatingActionButton fab_email;
    RecyclerView inbox_container;
    RelativeLayout no_item_layout;
    Button refresh;
    RelativeLayout refresh_layout;
    RelativeLayout topView;
    EmailAdapter adapter;
    com.google.api.services.gmail.Gmail mService = null;
    GoogleAccountCredential mCredential;
    MimeMessage mimeMessage;
    ProgressDialog mProgress;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String[] SCOPES = {
            GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.GMAIL_READONLY,
            GmailScopes.MAIL_GOOGLE_COM
    };

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

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Sending...");
        this.refresh.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                readMails();
            }
        });

        mService = new com.google.api.services.gmail.Gmail.Builder(
                AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), mCredential)
                .setApplicationName(getResources().getString(R.string.app_name))
                .build();

        this.mCredential = GoogleAccountCredential.usingOAuth2(getActivity().getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());

        fab_email.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                getResultsFromApi();
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

    private void getResultsFromApi() {
        Log.d(this.TAG, "getResultFromApi");
        if (!isGooglePlayServicesAvailable()) {
            Log.d(this.TAG, "Google api available");
            acquireGooglePlayServices();
        } else if (this.mCredential.getSelectedAccountName() == null) {
            Log.d(this.TAG, "Choose account");
            chooseAccount();
        } else if (isDeviceOnline()) {
            Log.d(this.TAG, "Making credential request");
            openSendEmailDialog();
        } else {
            Log.d(this.TAG, "Device is online");
            Toast.makeText(getActivity().getApplicationContext(),"No network connection available",Toast.LENGTH_LONG).show();
        }
    }

    private void openSendEmailDialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.dialog_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        final EditText recipient = (EditText) promptsView.findViewById(R.id.recipient);
        final EditText subject = (EditText) promptsView.findViewById(R.id.subject);
        final EditText body = (EditText) promptsView.findViewById(R.id.body);
        final Button sendEmail = (Button) promptsView.findViewById(R.id.sendEmail);

        alertDialogBuilder.setCancelable(true);
        sendEmail.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                if(recipient.getText().toString().matches("") || subject.getText().toString().matches("") || body.getText().toString().matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill all fields",Toast.LENGTH_LONG).show();
                }else{
                    Boolean isEmailValid = isValidEmail(recipient.getText().toString());
                    if(isEmailValid) {
                        new MakeRequestTask(getActivity(), mCredential).execute();
                    }
//                        try {
//                            String userId = "";
//                            mimeMessage = createEmail(recipient.getText().toString(), "ankurkhandelwal08027@gmail.com", subject.getText().toString(), body.getText().toString());
//                            Log.d(TAG,"createEmail = "+mimeMessage.getContent().toString());
//                            com.google.api.services.gmail.model.Message message = createMessageWithEmail(mimeMessage);
//                            Log.d(TAG,"message = "+message.toPrettyString());
//                            message =  mService.users().messages().send("me",message).execute();
//                            Log.d(TAG,"message = "+message.getId());
//                            Log.d(TAG,"message string = "+message.toPrettyString());
//                            Log.d(TAG,"message = "+message.getId());
////                            String response = sendMessage(mService, user, mimeMessage);
//                        } catch (MessagingException e) {
//                            Log.d(TAG, "dialog exception = "+e.getMessage());
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(),"Email is not valid",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    // Async Task for sending Mail using GMail OAuth
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {

        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
        private FragmentActivity fragmentMailsWeakReference;

        MakeRequestTask(FragmentActivity context, GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();
            this.fragmentMailsWeakReference = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private String getDataFromApi() throws IOException {
            // getting Values for to Address, from Address, Subject and Body
            String user = "me";
            String to = "ankur@non.sa";
            String from = mCredential.getSelectedAccountName();
            String subject = "test";
            String body = "test";
            MimeMessage mimeMessage;
            String response = "";
            try {
                mimeMessage = createEmail(to, from, subject, body);
                response = sendMessage(mService, user, mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return response;
        }

        // Method to send email
        private String sendMessage(Gmail service,
                                   String userId,
                                   MimeMessage email)
                throws MessagingException, IOException {
            com.google.api.services.gmail.model.Message message = createMessageWithEmail(email);
            // GMail's official method to send email with oauth2.0
            message = service.users().messages().send(userId, message).execute();

            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message.getId();
        }

        // Method to create email Params
        private MimeMessage createEmail(String to,
                                        String from,
                                        String subject,
                                        String bodyText) throws MessagingException {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);
            InternetAddress tAddress = new InternetAddress(to);
            InternetAddress fAddress = new InternetAddress(from);

            email.setFrom(fAddress);
            email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
            email.setSubject(subject);
            return email;
        }

        private com.google.api.services.gmail.model.Message createMessageWithEmail(MimeMessage email)
                throws MessagingException, IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            email.writeTo(bytes);
            String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
            com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
            message.setRaw(encodedEmail);
            return message;
        }

        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            mProgress.hide();
            if (output == null || output.length() == 0) {
                Toast.makeText(getActivity().getApplicationContext(), "No results returned.",Toast.LENGTH_LONG).show();
            } else {
                Log.d("Send mail success ",output);
                Toast.makeText(getActivity().getApplicationContext(), output,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            MainActivity activity = (MainActivity) fragmentMailsWeakReference;
            if (activity == null) return;
            if(mProgress != null && mProgress.isShowing()){
                mProgress.cancel();
            }
            if (this.mLastError != null) {
                Log.d("MailActivity", "Async OnCancelled");
                if (this.mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    Log.d("MailActivity", "GooglePlayServicesAvailabilityIOException");
//                    fragmentMailsWeakReference.get().showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) this.mLastError).getConnectionStatusCode());
                    return;
                } else if (this.mLastError instanceof UserRecoverableAuthIOException) {
                    Log.d("MailActivity", "UserRecoverableAuthIOException");
//                    fragmentMailsWeakReference.get().startActivityForResult(((UserRecoverableAuthIOException) this.mLastError).getIntent(), 1001);
                    return;
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "The following error occured "+mLastError.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                    return;
                }
            }
        }
    }




    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checkMails() {
        if (DataHungryApplication.emailArrayList.size() == 0){
            refresh_layout.setVisibility(View.VISIBLE);
            inbox_container.setVisibility(View.GONE);
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

    @AfterPermissionGranted(1003)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult = "+requestCode);
        switch (requestCode) {
            case 66536:
                if (resultCode == -1 && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra("authAccount");
                    if (accountName != null) {
                        SharedPreferences.Editor editor = getActivity().getPreferences(0).edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        this.mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                        return;
                    }
                    return;
                }
                return;
            case 1001:
                if (resultCode == -1) {
                    getResultsFromApi();
                    return;
                }
                return;
            case 1002:
                if (resultCode != -1) {
                    Toast.makeText(getActivity().getApplicationContext(),"This app requires Google Play Services. Please install Google Play Services on your device and relaunch this app.",Toast.LENGTH_LONG).show();
                    return;
                } else {
                    getResultsFromApi();
                    return;
                }
            default:
                return;
        }
    }


    @AfterPermissionGranted(1003)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(getActivity(), "android.permission.GET_ACCOUNTS")) {
            String accountName = getActivity().getPreferences(0).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                this.mCredential.setSelectedAccountName(accountName);
                Log.d(TAG,"selected_account ="+accountName);
                getResultsFromApi();
                return;
            }
            startActivityForResult(this.mCredential.newChooseAccountIntent(), 1000);
            return;
        }
        EasyPermissions.requestPermissions(getActivity(), "This app needs to access your Google account (via Contacts).", 1003, "android.permission.GET_ACCOUNTS");
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void onPermissionsGranted(int requestCode, List<String> list) {
    }

    public void onPermissionsDenied(int requestCode, List<String> list) {
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity()) == 0;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(int connectionStatusCode) {
        GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), connectionStatusCode, 1002).show();
    }
}
